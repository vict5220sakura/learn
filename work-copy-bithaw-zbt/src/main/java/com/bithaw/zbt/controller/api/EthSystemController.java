package com.bithaw.zbt.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bithaw.common.annotation.RpcAuthRequire;
import com.bithaw.zbt.service.EthSystemService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WangWei Eth系统接口
 * @date: 2018年8月24日 上午10:08:25
 * @version: v1.0.0
 * @Description:eth接口
 */
@Slf4j
@RestController
public class EthSystemController { 
	
	@Autowired
	private EthSystemService ethSystemService;
	
	/**
	 * @author WangWei
	 * @Description 根据orderNo查询交易状态,查询数据库
	 * @method getState
	 * @param orderNo
	 * @return String FAIL:失败;PENDING:打包中;SUCCESS:成功;OVERRIDE:被覆盖
	 * @date 2018年9月10日 下午4:42:17
	 */
	@PostMapping(value = "/eth/system/getState")
	public String getState(String orderNo){
		log.info("据orderNo查询交易状态:开始");
		int state = ethSystemService.getState(orderNo);
		if(state == -1){
			log.info("据orderNo查询交易状态:结束,orderNo{},state{}",orderNo,"失败");
			return "FAIL";
		}
		if(state == 0){
			log.info("据orderNo查询交易状态:结束,orderNo{},state{}",orderNo,"打包中");
			return "PENDING";
		}
		if(state == 1){
			log.info("据orderNo查询交易状态:结束,orderNo{},state{}",orderNo,"成功");
			return "SUCCESS";
		}
		if(state == -2){
			log.info("据orderNo查询交易状态:结束,orderNo{},state{}",orderNo,"被覆盖");
			return "OVERRIDE";
		}
		log.info("据orderNo查询交易状态:结束,orderNo{},state{}",orderNo,"查询失败返回失败");
		return "FAIL";
	}

	/**  
	 * 检查节点状态
	 * @author: WangWei
	 * @date: 2018年8月24日 上午10:14:31 
	 * @version: v1.0.0
	 * @Description:  void 检查节点状态 存入数据库配置
	 */
	@PostMapping("/eth/system/checkNode")
	public void checkNode(){
		log.info("检查eth接口");
		ethSystemService.checkLocalNode();
	}

	/**
	 * @author WangWei
	 * @Description 定时设置nonce任务,有分布式锁
	 * @method setNonceTask 
	 * @return void
	 * @date 2018年9月10日 下午4:37:34
	 */
	@PostMapping(value = "/eth/system/setNonceTask")
	public void setNonceTask(){
		log.info("设置nonce任务开始");
		ethSystemService.setNonceTask();
	}
	
	/**
	 * @author WangWei
	 * @Description 本地签名任务,有分布式锁
	 * @method localSignTask 
	 * @return void
	 * @date 2018年9月10日 下午4:38:47
	 */
	@PostMapping(value = "/eth/system/localSignTask")
	public void localSignTask(){
		log.info("本地签名任务开始");
		ethSystemService.localSignTask();
	}
	
	/**  
	 * 扫描EthTradeNonce状态为0的交易发送到以太坊公链,有分布式锁
	 * @author: WangWei
	 * @date: 2018年8月24日 上午10:00:55 
	 * @version: v1.0.0
	 * @Description:  扫描EthTradeNonce状态为0的交易发送到以太坊公链
	 */
	@PostMapping(value = "/eth/system/sendTask")
	public void sendTask(){
		log.info("扫描EthTradeNonce状态为0的交易发送 开始");
		ethSystemService.sendTask();
		log.info("扫描EthTradeNonce状态为0的交易发送 结束");
	}
	
	/**
	 * @author WangWei
	 * @Description 补全txhash任务,没有分布式锁
	 * @method scanAndEnsureTxhash 
	 * @return void
	 * @date 2018年9月10日 下午4:42:00
	 */
	@PostMapping(value = "/eth/system/etherscanRepairTxhashTask")
	public void etherscanRepairTxhashTask(){
		log.info("查到没有txhash的交易,并根据交易信息上etherscan查找信息补全txhash:开始");
		ethSystemService.etherscanRepairTxhashTask();
		log.info("查到没有txhash的交易,并根据交易信息上etherscan查找信息补全txhash:结束");
	}

	/**
	 * @author WangWei
	 * @Description 确认交易任务,没有分布式锁
	 * @method scanAndEnsureStateNot1 
	 * @return void
	 * @date 2018年9月10日 下午4:41:24
	 */
	@PostMapping(value = "/eth/system/etherscanEnsureTxTask")
	public void etherscanEnsureTxTask(){
		log.info("扫描状态不是1的交易并确认交易是否成功 开始");
		ethSystemService.ensureTxByEtherscanTask();
		log.info("扫描状态不是1的交易并确认交易是否成功 结束");
	}
}















/*  
 * 代币转账,签名交易 已废弃
 * @author: WangWei
 * @date: 2018年8月24日 上午10:09:55 
 * @version: 废弃
 * @Description: 
 * @param fromAddress
 * @param mnemonic
 * @param password
 * @param toAddress
 * @param amount
 * @param solidityAddress
 * @return String 
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年8月24日      WangWei           v1.0.0 			创建
 * 2018年8月24日	WangWei				废弃				废弃
 */
/*@Deprecated
@PostMapping(value = "/eth/zbtTrade")
@RpcAuthRequire
public String zbtTrade(
		@Param("fromAddress") String fromAddress, 
		@Param("mnemonic") String mnemonic, 
		@Param("password") String password, 
		@Param("toAddress") String toAddress, 
		@Param("amount") String amount,
		@Param("solidityAddress") String solidityAddress
		) {
	log.info("调用eth/zbtTrade接口");
	JSONObject returnjsonObject = new JSONObject();
	try{
		String gasPrice = sysConfigClient.getSysConfigValue("eth_gasprice");
		//查询账户代币余额
		BigInteger fromToken = ethService.getTokenBalance(fromAddress, solidityAddress);
		//查询账户eth余额
		BigDecimal fromBalance = ethService.getBalance(fromAddress).multiply(new BigDecimal("1000000000000000000"));
		log.info("用户账号 : " + fromAddress + " , 用户eth余额 : " + fromBalance + "(eth)");
		log.info("用户账号 : " + fromAddress + " , 用户代币余额 : " + fromToken);
		
		if(fromBalance.compareTo(new BigDecimal(gasPrice).multiply(new BigDecimal("1000000000"))) < 0){
			returnjsonObject.put("code", 4);
			returnjsonObject.put("transactionHash", "");
			returnjsonObject.put("message", "eth余额不足");
			log.info("调用eth/zbtTrade接口:eth余额不足");
			return returnjsonObject.toJSONString();
		}
		
		if(fromToken.compareTo(new BigDecimal(amount).multiply(new BigDecimal("1000000")).toBigInteger()) < 0){
			returnjsonObject.put("code", 2);
			returnjsonObject.put("transactionHash", "");
			returnjsonObject.put("message", "余额不足");
			log.info("调用eth/zbtTrade接口:zbt余额不足");
			return returnjsonObject.toJSONString();
		}
		BigDecimal gwei = new BigDecimal("1000000000");
		String transactionHash = ethService.sendTokenOutline(fromAddress//
				, mnemonic, password, toAddress, solidityAddress//
				, new BigDecimal(amount).multiply(new BigDecimal("1000000")).toBigInteger()//
				,new BigInteger(gasPrice).multiply(gwei.toBigInteger()));
		
		if(StringUtils.isBlank(transactionHash)){
			returnjsonObject.put("code", 3);
			returnjsonObject.put("transactionHash", "");
			returnjsonObject.put("message", "交易失败(请检查gas是否充足)");
			log.info("调用eth/zbtTrade接口:交易失败(请检查gas是否充足)");
			return returnjsonObject.toJSONString();
		}
		returnjsonObject.put("code", 0);
		returnjsonObject.put("transactionHash", transactionHash);
		returnjsonObject.put("message", "成功");
		log.info("调用eth/zbtTrade接口:交易成功");
		return returnjsonObject.toJSONString();
	}catch(Exception e){
		log.error("调用eth/zbtTrade接口:交易失败",e);
		returnjsonObject.put("code", 1);
		returnjsonObject.put("transactionHash", "");
		returnjsonObject.put("message", e.getMessage());
		return returnjsonObject.toJSONString();
	}
}*/
/*
 * JSONObject构造器
 * @author WangWei
 * @date: 2018年8月24日 上午9:52:30
 * @version: v1.0.0
 * @Description:构造者模式构造JSONObject
 */
/*public class JSONBuilder{
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
}*/
/*  
 * 以太币转账,签名交易
 * @author: WangWei
 * @date: 2018年8月24日 上午10:11:27 
 * @version: v1.0.0
 * @Description: 
 * @param fromAddress
 * @param mnemonic
 * @param password
 * @param toAddress
 * @param amount 以太币(eth) (1以太币=1(18个0)wei)
 * @return String 
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年8月24日      WangWei           v1.0.0 			创建
 */
/*@PostMapping(value = "/eth/ethTrade")
@RpcAuthRequire
public String ethTrade(// 王玮 需要被替换
		@Param("fromAddress") String fromAddress, 
		@Param("mnemonic") String mnemonic, 
		@Param("password") String password, 
		@Param("toAddress") String toAddress, 
		@Param("amount") String amount
		) {
	log.info("调用eth/ethTrade接口");
	JSONObject returnjsonObject = new JSONObject();
	try{
		String gasPrice = sysConfigClient.getSysConfigValue("eth_gasprice");
		//查询账户eth余额
		BigDecimal fromBalance = ethService.getBalance(fromAddress);
		log.info("用户账号 : " + fromAddress + " , 用户eth余额 : " + fromBalance + "(eth)");
		BigDecimal eth = new BigDecimal("1000000000000000000");
		BigDecimal gwei = new BigDecimal("1000000000");
		BigDecimal gasLimit = new BigDecimal("500000");
		if(fromBalance.multiply(eth).compareTo(//
				new BigDecimal(amount).multiply(eth).add(//
						new BigDecimal(gasPrice).multiply(gwei).multiply(gasLimit)//
						)//
				) //
				< 0){
			returnjsonObject.put("code", 2);
			returnjsonObject.put("transactionHash", "");
			returnjsonObject.put("message", "eth余额不足");
			log.info("调用eth/ethTrade接口:余额不足 " + fromBalance + "eth");
			return returnjsonObject.toJSONString();
		}
		String transactionHash = ethService.sendEthOutline(fromAddress,//
				mnemonic, password, toAddress,//
				new BigDecimal(amount).multiply(new BigDecimal("1000000000000000000")).toBigInteger(),//
				new BigInteger(gasPrice).multiply(gwei.toBigInteger()));
		
		if(StringUtils.isBlank(transactionHash)){
			returnjsonObject.put("code", 3);
			returnjsonObject.put("transactionHash", "");
			returnjsonObject.put("message", "交易失败(请检查gas是否充足)");
			log.info("调用eth/ethTrade接口:交易失败(请检查gas是否充足) ");
			return returnjsonObject.toJSONString();
		}
		returnjsonObject.put("code", 0);
		returnjsonObject.put("transactionHash", transactionHash);
		returnjsonObject.put("message", "成功");
		log.info("调用eth/ethTrade接口:交易成功");
		return returnjsonObject.toJSONString();
	}catch(Exception e){
		log.error("交易失败",e);
		returnjsonObject.put("code", 1);
		returnjsonObject.put("transactionHash", "");
		returnjsonObject.put("message", "系统错误交易失败");
		return returnjsonObject.toJSONString();
	}
}*/
/*  
 * 查询eth账户余额
 * @author: WangWei
 * @date: 2018年8月24日 上午10:12:33 
 * @version: v1.0.0
 * @Description: 
 * @param address
 * @return String  (eth) 
 */
/*@PostMapping(value = "/eth/getEth")
@RpcAuthRequire
public String getEth(@Param("address") String address){
	log.info("调用eth/getEth接口");
	try {
		BigDecimal bigDecimal = ethService.getBalance(address);
		log.info("调用eth/getEth接口:成功");
		return bigDecimal.toPlainString();
	} catch (IOException e) {
		e.printStackTrace();
		log.error("查询失败",e);
	}
	return "0";
}*/
/*  
 * 查询zbt账户余额
 * @author: WangWei
 * @date: 2018年8月24日 上午10:12:47 
 * @version: v1.0.0
 * @Description: 
 * @param address
 * @param solidityAddress
 * @return String (B) 
 *//*
@PostMapping(value = "/eth/getZbt")
@RpcAuthRequire
public String getZbt(@Param("address") String address,@Param("solidityAddress") String solidityAddress){
	log.info("调用eth/getZbt接口");
	BigInteger tokenBalance = ethService.getTokenBalance(address, solidityAddress);
	String zbtBalance = new BigDecimal(tokenBalance.toString())//
			.divide(new BigDecimal("1000000"))//
			.toPlainString();
	log.info("调用eth/getZbt接口:成功");
	return zbtBalance;
}*/

/*  
 * 查询交易状态 废弃
 * @author: WangWei
 * @date: 2018年8月24日 上午10:13:24 
 * @version: v1.0.0
 * @Description: 
 * @param transactionHash
 * @return String 
 * Modification History:
 * Date         Author          Version            Description
 *---------------------------------------------------------*
 * 2018年8月24日      WangWei           v1.0.0 			废弃
 */
/*@Deprecated
@PostMapping(value = "/eth/getTradeStatus")
@RpcAuthRequire
public String getTradeStatus(@Param("transactionHash") String transactionHash){
	log.info("调用eth/getTradeStatus接口");
	try{
		Transaction transaction = ethService.getTransactionByHash(transactionHash);
		if(transaction == null){
			log.info("调用eth/getTradeStatus接口:没有查到交易,如果交易正常则交易处于挂起状态,等待被旷工挖到(后续交易可能失败,被覆盖,gasprice太小被取消)");
			return "PENDING";
		}
		
		TransactionReceipt transactionReceipt = ethService.getTransactionReceipt(transactionHash);
		
		if(!transactionReceipt.isStatusOK()){
			log.info("调用eth/getTradeStatus接口:查询到交易状态,交易状态显示失败,交易gas消耗光但还没有执行完");
			return "FAIL";
		}
		
		if(ethService.getBlockNumber().intValue() - transactionReceipt.getBlockNumber().intValue() >= 6){
			log.info("调用eth/getTradeStatus接口:查询到交易状态,交易状态显示成功,切被6区块确认");
			return "SUCCESS";
		}else{
			log.info("调用eth/getTradeStatus接口:查询到交易状态,交易状态显示成功,还没有6个区块确认");
			return "TRADING";
		}
		
	} catch (Throwable e) {
		e.printStackTrace();
		log.error("调用eth/getTradeStatus接口:查询交易状态失败",e);
		return "QUERYFAIL";
	}
}*/
/*  
 * 查询交易手续费
 * @author: WangWei
 * @date: 2018年8月24日 上午10:14:07 
 * @version: v1.0.0
 * @Description: 
 * @param transactionHash
 * @return String 交易手续费(string)(单位:eth)/"":查询失败
 */
/*@PostMapping(value = "/eth/getTradeFee")
@RpcAuthRequire
public String getTradeFee(@Param("transactionHash") String transactionHash){
	log.info("调用eth/getTradeFee接口:查询交易手续费");
	try {
		TransactionReceipt transactionReceipt = ethService.getTransactionReceipt(transactionHash);
    	Transaction transaction = ethService.getTransactionByHash(transactionHash);
    	String feeWei = transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()).toString();
    	String feeEth = new BigDecimal(feeWei).divide(new BigDecimal("1000000000000000000")).toPlainString();
    	log.info("调用eth/getTradeFee接口:查询交易手续费成功");
    	return feeEth;
	} catch (Exception e) {
		e.printStackTrace();
		log.error("调用eth/getTradeFee接口:查询交易手续费失败",e);
		return "";
	}
}*/