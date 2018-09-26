/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午1:52:42
 * @version   V 1.0
 */
package com.bithaw.zbt.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.zbt.common.Common;
import com.bithaw.zbt.entity.EthTradeNonce;
import com.bithaw.zbt.entity.EthTradeNonceCover;
import com.bithaw.zbt.feign.SysConfigClient;
import com.bithaw.zbt.mapper.EthTradeNonceCoverMapper;
import com.bithaw.zbt.mapper.EthTradeNonceMapper;
import com.bithaw.zbt.service.EthSystemService;
import com.bithaw.zbt.service.EthTradeService;
import com.bithaw.zbt.solidity.bhaw.v1.BhawV1;
import com.bithaw.zbt.utils.eth.EthWeb3jUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 以太坊交易功能接口
 * @author   WangWei
 * @date     2018年9月12日 下午1:52:42
 * @version  V 1.0
 */
@Slf4j
@Service
public class EthTradeServiceImpl implements EthTradeService{
	
	@Autowired
	private BhawV1 bhawV1;
	
	@Autowired
	private EthSystemService ethSystemService;
	
	@Autowired
	private SysConfigClient sysConfigClient;
	
	@Autowired
	private EthTradeNonceMapper ethTradeNonceMapper;
	
	@Autowired
	private EthTradeNonceCoverMapper ethTradeNonceCoverMapper;
	
	@Autowired
	private EthWeb3jUtil ethWeb3jUtil;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private String[] ethCallStrategys;
	/**
	 * @Description 构造者模式构造JSONObject
	 * @author   WangWei
	 * @date     2018年9月10日 上午9:47:41
	 * @version  V 1.0
	 */
	public class JSONBuilder{
		private JSONObject jsonObject;
		JSONBuilder(){
			this.jsonObject = new JSONObject();
		}
		private JSONBuilder put(String key,String value){
			this.jsonObject.put(key, value);
			return this;
		}
		private JSONBuilder put(String key,int value){
			this.jsonObject.put(key, value);
			return this;
		}
		public JSONObject build(){
			return this.jsonObject;
		}
	}
	
	/** 
	 * <p>Title: init</p>
	 * <p>Description: 初始化</p>
	 * @see com.bithaw.zbt.service.EthTradeService#init()  
	 */
	@Override
	public void init(){
		log.info("初始化开始");
		//solidity地址最佳由代码决定 solidityAddress = sysConfigClient.getSysConfigValue("zbt_solidity_address");
		String ethCallStrategyStr = sysConfigClient.getSysConfigValue("eth_call_strategy");
		ethCallStrategys = ethCallStrategyStr.split("\\|");
		log.info("初始化结束");
	}
	
	/** 
	 * <p>Title: newAccount</p>
	 * <p>Description: 创建eth账号</p>
	 * @return 公钥|助记词
	 * @see com.bithaw.zbt.service.EthSystemService#newAccount()  
	 */
	@Override
	public String newAccount(String password) {
		try {
			return ethWeb3jUtil.newEthAccount(password);
		} catch (Exception e) {
			log.error("eth创建账户失败");
			return "";
		}
	}
	
	/** 
	 * <p>Title: zbtTradeTransaction</p>
	 * <p>Description: eth通用交易</p>
	 * @param orderNo
	 * @param fromAddress
	 * @param toAddress
	 * @param amount
	 * @return
	 * @see com.bithaw.zbt.service.EthTradeService#zbtTradeTransaction(java.lang.String, java.lang.String, java.lang.String, java.lang.String)  
	 */
	private void ethTransaction(String orderNo,String fromAddress, String toAddress,BigDecimal value,String data) {
		
		String gasPriceStr = sysConfigClient.getSysConfigValue("eth_gasprice");//gwei
		BigInteger gasPrice = new BigInteger(gasPriceStr).multiply(Common.B1x10_9.toBigInteger());
		BigDecimal gasPriceBigDecimal = new BigDecimal(gasPrice.toString()).divide(Common.B1x10_9, 9, BigDecimal.ROUND_HALF_UP);
		
		BigInteger gasLimit = BigInteger.valueOf(500000L);
		
		EthTradeNonce ethTradeNonce = new EthTradeNonce.Builder()
				.setOrderNo(orderNo)
				.setFromAddress(fromAddress)
				.setToAddress(toAddress)
				.setData(data)
				.setCreateTime(new Date())
				.setGasLimit(gasLimit.longValue())
				.setGasPrice(gasPriceBigDecimal)
				.setState(0)
				.setValue(value)
				.build();
		
		ethTradeNonceMapper.save(ethTradeNonce);
	}
	
	/** 
	 * <p>Title: ethCoverTransaction</p>
	 * <p>Description: eth通用覆盖交易</p>
	 * @param coverNo 订单号 
	 * @param orderNo 原交易订单号
	 * @param gasPriceStr (gwei)
	 * @param toAddress 覆盖的目的地地址
	 * @param value 覆盖的金额
	 * @param data 覆盖的内容
	 * @return true:覆盖交易发送成功;false:发送失败:原交易已经成功
	 * @see com.bithaw.zbt.service.EthTradeService#ethCoverTransaction(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.math.BigDecimal, java.lang.String)  
	 */
	@Override
	@Transactional
	public boolean ethCoverTransaction(String coverNo ,String orderNo , String gasPriceStr , String toAddress , BigDecimal value , String data) {
		
		if(ethSystemService.getState(orderNo) == -1 ){//订单不存在
			throw new RuntimeException("覆盖交易出错,不存在的交易");
		}
		
		EthTradeNonce ethTradeNonce = ethTradeNonceMapper.findByorderNo(orderNo);
		
		if(ethTradeNonce == null){
			log.warn("覆盖交易,原交易不存在");
			return false;
		}
		
		if(ethTradeNonce.getState() == 1 || ethTradeNonce.getState() == 5){
			log.info("order:{};的原交易覆盖失败;原交易已经成功上链,或成功被覆盖",orderNo);
			return false;
		}
		
		if( ethTradeNonce.getGasPrice().compareTo(new BigDecimal(gasPriceStr) ) == 0 
				&& ethTradeNonce.getToAddress().toLowerCase().equals(toAddress.toLowerCase())
				&& ethTradeNonce.getValue().compareTo(value) == 0
				&& ethTradeNonce.getData().toLowerCase().equals(data.toLowerCase())){
			//覆盖交易与原交易内容完全一样
			log.warn("覆盖交易,交易内容与原交易一样");
			return false;
		}
		
		EthTradeNonceCover sameCoverBean = ethTradeNonceCoverMapper.findSameParam(
				orderNo
				,ethTradeNonce.getFromAddress()
				, toAddress
				, value
				, new BigDecimal(gasPriceStr)
				, data);
		if( sameCoverBean != null ){
			log.warn("覆盖交易,已经存在一样的覆盖交易");
			return false;
		}
		
		ethTradeNonce.setCoverState(1);//添加原交易覆盖交易标记,作为限制其继续发送标记
		ethTradeNonceMapper.save(ethTradeNonce);
		
		//扫描添加已有覆盖交易,添加交易覆盖标记,作为限制其继续发送标记
		List<EthTradeNonceCover> alreadyExistBean = ethTradeNonceCoverMapper.findAllByOrderNo(orderNo);
		for(EthTradeNonceCover bean : alreadyExistBean){
			bean.setCoverState(1);
			ethTradeNonceCoverMapper.save(bean);
		}
		
		BigInteger gasPrice = new BigInteger(gasPriceStr).multiply(Common.B1x10_9.toBigInteger());
		BigDecimal gasPriceBigDecimal = new BigDecimal(gasPrice.toString()).divide(Common.B1x10_9, 9, BigDecimal.ROUND_HALF_UP);
		
		BigInteger gasLimit = BigInteger.valueOf(500000L);
		
		EthTradeNonceCover ethTradeNonceCover = new EthTradeNonceCover().builder()
				.coverNo(coverNo)
				.fromAddress(ethTradeNonce.getFromAddress())
				.orderNo(orderNo)
				.toAddress(toAddress)
				.data(data)
				.createTime(new Date())
				.gasLimit(gasLimit.longValue())
				.gasPrice(gasPriceBigDecimal)
				.state(0)
				.value(value)
				.build();
		
		ethTradeNonceCoverMapper.save(ethTradeNonceCover);
		log.info("覆盖交易发送成功,coverNo:{}",coverNo);
		return true;
	}

	/** 
	 * <p>Title: zbtTrade</p>
	 * <p>Description: zbt转账,存储转账记录到数据库,设置状态为0:打包中</p>
	 * @param orderNo
	 * @param fromAddress
	 * @param toAddress
	 * @param amount
	 * @return
	 * @see com.bithaw.zbt.service.EthTradeService#zbtTrade(java.lang.String, java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public String zbtTrade(String orderNo,String fromAddress, String toAddress, String amount) {
		log.info("zbt转账开始,toaddress {} amount {}",toAddress,amount);
		try {
			int state = ethSystemService.getState(orderNo);
			if( state == -1 ){
				log.info("发起一笔zbt转账,交易不存在,进行转账逻辑");
			}else if( state == 0){//已经存在交易
				return new JSONBuilder().put("code", 0).put("message","交易发送成功").build().toJSONString();
			}else if( state == 1){
				return new JSONBuilder().put("code", 1).put("message","交易已存在").build().toJSONString();
			}else if( state == -2){
				return new JSONBuilder().put("code", 1).put("message","交易已经被覆盖").build().toJSONString();
			}
			
			BigInteger amountBiginteger = new BigDecimal(amount).multiply(Common.B1x10_6).toBigInteger();
			Optional<String> data = bhawV1.bhawV1CreateTransactionData.transfer(toAddress, amountBiginteger);
			String dataHex = data.orElseThrow(() -> new RuntimeException("合约创建data失败"));
			ethTransaction(orderNo, fromAddress, BhawV1.solidityAddress, BigDecimal.ZERO, dataHex);
			
			return new JSONBuilder().put("code", 0).put("message","交易发送成功").build().toJSONString();
		} catch (Exception e) {
			return new JSONBuilder().put("code", 1).put("message",e.getMessage()).build().toJSONString();
		}
	}

	/** 
	 * <p>Title: zbtTradeCover</p>
	 * <p>Description: 发起一笔zbt覆盖交易</p>
	 * @param coverNo
	 * @param orderNo
	 * @param gasPriceStr (gwei)
	 * @param toAddress
	 * @param amount
	 * @return FAIL:覆盖失败
	 * @see com.bithaw.zbt.service.EthTradeService#zbtTradeCover(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public String zbtTradeCover(String coverNo,String orderNo, String gasPriceStr,String toAddress, String amount) {
		log.info("zbt转账覆盖开始,toaddress {} amount {}",toAddress,amount);
		try{
			BigInteger amountBiginteger = new BigDecimal(amount).multiply(Common.B1x10_6).toBigInteger();
			Optional<String> data = bhawV1.bhawV1CreateTransactionData.transfer(toAddress, amountBiginteger);
			EthTradeService thisBean = applicationContext.getBean(EthTradeService.class);
			boolean ethCoverFlag = thisBean.ethCoverTransaction(coverNo,orderNo, gasPriceStr, BhawV1.solidityAddress, BigDecimal.ZERO, data.orElseThrow(() -> new RuntimeException("zbt转账覆盖,出错,data空指针")));
			if(!ethCoverFlag){
				return "FAIL";
			}
			return "SUCCESS";
		}catch(Exception e){
			return "ERROR";
		}
	}
	
	/** 
	 * <p>Title: selectFreeze</p>
	 * <p>Description: 查询冻结状态</p>
	 * @param address
	 * @return
	 * @see com.bithaw.zbt.service.EthTradeService#selectFreeze(java.lang.String)  
	 */
	@Override
	public String selectFreeze(String address) {
		for(String strategy : ethCallStrategys){
			switch (strategy) {
			case "local":
				try {
					Boolean flag = bhawV1.bhawV1SelectByWeb3j.freezeFlag(address, address).orElseThrow(() -> new Exception("本地节点查询状态失败"));
					if(flag)
						return "true";
					else
						return "false";
				} catch (Exception e) {
					break;
				}
			case "etherscan":
				try {
					Boolean flag = bhawV1.bhawV1SelectByEtherscan.freezeFlag(address).orElseThrow(() -> new Exception("etherscan节点查询状态失败"));
					if(flag)
						return "true";
					else
						return "false";
				} catch (Exception e) {
					break;
				}
			}
		}
		throw new RuntimeException("查询状态失败");
	}

	/** 
	 * <p>Title: zbtAccountFreeze</p>
	 * <p>Description: 冻结</p>
	 * @param orderNo
	 * @param fromAddress 管理员账号
	 * @param address
	 * @return
	 * @see com.bithaw.zbt.service.EthTradeService#zbtAccountFreeze(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public String zbtAccountFreeze(String orderNo, String fromAddress, String address) {
		log.info("冻结");
		try{
			if(ethSystemService.getState(orderNo) == -1 ){

			}else{//已经存在交易
				return "SUCCESS";
			}
			
			String data = bhawV1.bhawV1CreateTransactionData.freezeAccount(address, true).orElseThrow(() -> new RuntimeException("合约创建data失败"));
			
			ethTransaction(orderNo, fromAddress, BhawV1.solidityAddress, BigDecimal.ZERO, data);
			
			return "SUCCESS";
		}catch (Exception e){
			return "FAIL";
		}
	}
	
	/** 
	 * <p>Title: zbtAccountFreeze</p>
	 * <p>Description: 冻结</p>
	 * @param orderNo
	 * @param fromAddress 管理员账号
	 * @param address
	 * @return
	 * @see com.bithaw.zbt.service.EthTradeService#zbtAccountFreeze(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public String zbtAccountUnfreeze(String orderNo, String fromAddress, String address) {
		log.info("冻结");
		try{
			if(ethSystemService.getState(orderNo) == -1 ){
				
			}else{//已经存在交易
				return "SUCCESS";
			}
			
			String data = bhawV1.bhawV1CreateTransactionData.freezeAccount(address, false).orElseThrow(() -> new RuntimeException("合约创建data失败"));
			
			ethTransaction(orderNo, fromAddress, BhawV1.solidityAddress, BigDecimal.ZERO, data);
			
			return "SUCCESS";
		}catch (Exception e){
			return "FAIL";
		}
	}
	
}
