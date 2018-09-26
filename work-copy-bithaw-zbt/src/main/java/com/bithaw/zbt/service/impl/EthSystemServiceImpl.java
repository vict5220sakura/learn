package com.bithaw.zbt.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bithaw.zbt.common.Common;
import com.bithaw.zbt.entity.EthTradeNonce;
import com.bithaw.zbt.exception.FindUnUseNonceException;
import com.bithaw.zbt.feign.SysConfigClient;
import com.bithaw.zbt.mapper.EthTradeNonceMapper;
import com.bithaw.zbt.service.EthSystemService;
import com.bithaw.zbt.utils.RedisComponent;
import com.bithaw.zbt.utils.eth.EthWeb3jLocolNodeUtil;
import com.bithaw.zbt.utils.eth.EtherscanApiUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据数据库存储交易信息广播一笔交易,更新状态
 * @author 王玮
 * 
 */
@Slf4j
@Service
public class EthSystemServiceImpl implements EthSystemService {
	@Autowired
	private EthTradeNonceMapper ethTradeNonceMapper;
	@Autowired
	private SysConfigClient sysConfigClient;
	@Autowired
    private RedisComponent redisComponent;
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private EthWeb3jLocolNodeUtil ethWeb3jLocolNodeUtil;
	
	@Autowired
	private EtherscanApiUtil etherscanApiUtil;
	
	/**
	 * localPriKey : 本地数据库<公钥,私钥>
	 */
	private Map<String,String> localPriKeyMap;
	
	@Value("${spring.profiles.active}")
	private String active;
	
	/** 
	 * <p>Title: 初始化</p>
	 * <p>Description: 初始化</p>
	 * @see com.bithaw.zbt.service.EthSystemService#init()  
	 */
	@Override
	public void init() {
		log.info("初始化 开始");
		String localPriKeys = sysConfigClient.getSysConfigValue("eth_company_address_prikey");
		localPriKeyMap = new HashMap<String, String>();
		
		for(String localPriKey : localPriKeys.split("\\|")){
			Credentials credentials = Credentials.create(localPriKey);
			localPriKeyMap.put(credentials.getAddress(), localPriKey);
		}
	}
	
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
	 * <p>Title: checkLocalNode</p>
	 * <p>Description: </p>
	 * @see com.bithaw.zbt.service.EthSystemService#checkLocalNode()  
	 */
	@Override
	public void checkLocalNode() {
		boolean flag = ethWeb3jLocolNodeUtil.checkLocalNode();
		if(flag){
			sysConfigClient.setSysConfigValue("eth_node_state", "ok");
		}else{
			sysConfigClient.setSysConfigValue("eth_node_state", "fail");
		}
	}

	/** 
	 * <p>Title: getState</p>
	 * <p>Description: 获取一笔交易的状态-1:不存在;0:打包中;1:交易成功;-2:交易被覆盖;</p>
	 * @param orderNo
	 * @return
	 * @see com.bithaw.zbt.service.EthSystemService#getState(java.lang.String)  
	 */
	@Override
	public int getState(String orderNo) {
		log.info("获取状态开始,orderNo {}",orderNo);
		try {
			EthTradeNonce ethTradeNonce = ethTradeNonceMapper.findByorderNo(orderNo);
			if(ethTradeNonce == null){
				return -1;
			}
			if(ethTradeNonce.getState() == 5){
				return -2;
			}
			if(ethTradeNonce.getState() == 1){
				return 1;
			}
			return 0;
		} catch(RuntimeException e){
			log.error("获取状态 结束,orderNo {};状态 {}",orderNo,"异常",e);
			throw e;
		}finally{
			log.info("获取状态 结束,orderNo {}",orderNo);
		}
	}

	/** 
	 * <p>Title: 设置nonce定时任务</p>
	 * <p>Description: state = 0的交易,设置nonce,设置state=3</p>
	 * @see com.bithaw.zbt.service.EthSystemService#setNonceTask()  
	 */
	@Override
	public void setNonceTask() {
		EthSystemService thisService = applicationContext.getBean(this.getClass());
		thisService.setNonceAsync();
	}

	@Async
	@Override
	public void setNonceAsync() {
		log.info("setNonceAsync异步任务开始");
		String redisKey = active + "_ETH_setNonce_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,setNonceAsync任务开始",redisKey);
				setNonceStart();
			} catch (Throwable e) {
				log.error("setNonceAsync出错",e);
			} finally {
				log.info("释放锁:{}",redisKey);
				redisComponent.release(redisKey);
			}
		}else{
			log.info("被锁定,忽略执行, 当前线程锁: {}",redisKey);
		}
	}



	/**
	 * @author WangWei
	 * @Description 获取state为0的全部交易,设置nonce值;分布式锁保证单线程执行,nonce数值安全
	 * @method startSetNonce 
	 * @return void
	 * @date 2018年9月10日 上午11:03:09
	 */
	private void setNonceStart() {
		List<String> fromAddresses = ethTradeNonceMapper.findFromAddressesList();
		EthSystemService thisService = applicationContext.getBean(this.getClass());
		for(String fromAddress : fromAddresses){
			thisService.setNonce(fromAddress);
		}
	}


	/**
	 * @author WangWei
	 * @Description 设置一个地址的nonce
	 * @method setNonce
	 * @param fromAddress 
	 * @return void
	 * @date 2018年9月10日 上午11:26:00
	 */
	@Override
	@Transactional
	public void setNonce(String fromAddress) {
		try {
			int nonce = findUnUseNonce(fromAddress);
			List<EthTradeNonce> ethTradeNonces = ethTradeNonceMapper.findAllByState(0);
			for(EthTradeNonce ethTradeNonce : ethTradeNonces){
				ethTradeNonce.setNonce(nonce);
				ethTradeNonce.setState(3);
				nonce++;
				ethTradeNonceMapper.save(ethTradeNonce);
			}
		} catch (FindUnUseNonceException e) {
			log.error("查询nonce出错");
		}
	}



	/**
	 * 找到一个地址最小可用的nonce
	 * 如果数据库无数据,访问链条获取一个nonce
	 * 遍历数据库nonce
	 * @throws FindUnUseNonceException 
	 */
	private int findUnUseNonce(String address) throws FindUnUseNonceException {
		log.info("找到一个可用的nonce:开始,address{}",address);
		List<Integer> nonces = ethTradeNonceMapper.findNonceByFromAddress(address);//耗时
		if(nonces.size() == 0){//耗时
			log.info("找到下一个可用的nonce:数据库不存在此nonce,address{}",address);
			String ethSendFindNonceStrategy = sysConfigClient.getSysConfigValue("eth_send_findNonce_strategy");
			log.info("找到下一个可用的nonce:查找账号可用nonce,address{},策略{}",address,ethSendFindNonceStrategy);
			switch (ethSendFindNonceStrategy) {
			case "etherscan":
				log.info("找到下一个可用的nonce:etherscan查找账号可用nonce,address{},策略{}",address,ethSendFindNonceStrategy);
				return etherscanApiUtil.getNonce(address);
			case "local":
				try {
					log.info("找到下一个可用的nonce:本地查找账号可用nonce,address{},策略{}",address,ethSendFindNonceStrategy);
					return ethWeb3jLocolNodeUtil.getNonceLocal(address);
				} catch (IOException e) {
					log.error("找到下一个可用的nonce:本地查找账号可用nonce:出错,address{},策略{}",address,ethSendFindNonceStrategy,e);
				}
			}
		}
		
		if(nonces.size() != 0){
			log.info("找到下一个可用的nonce:遍历查找一个可用nonce,address{}",address);
			Collections.sort(nonces);
			for(int i = nonces.get(0) ;  ; i++){
				if(nonces.contains((Integer)i)){
					continue;
				}else{
					return i;
				}
			}
		}
		throw new FindUnUseNonceException("查找nonce出错");
	}

	/** 
	 * <p>Title: localSignTask</p>
	 * <p>Description: 查找state = 3 的交易,根据时间排序,根据数据库本地私钥进行签名,将签名后的数据写回数据库,改state = 4</p>
	 * @see com.bithaw.zbt.service.EthSystemService#localSignTask()  
	 */
	@Override
	public void localSignTask() {
		String ethLocalSignFlag = sysConfigClient.getSysConfigValue("eth_local_sign_flag");
		if( !Boolean.parseBoolean(ethLocalSignFlag) ){
			log.info("本地签名未开");
			return;
		}
		
		EthSystemService thisBean = applicationContext.getBean(this.getClass());
		thisBean.localSignAsync();
	}
	
	/** 
	 * <p>Title: localSignAsync</p>
	 * <p>Description: 异步任务</p>
	 * @see com.bithaw.zbt.service.EthSystemService#localSignAsync()  
	 */
	@Async
	@Override
	public void localSignAsync() {
		log.info("localSignAsync异步任务开始");
		String redisKey = active + "_ETH_localSign_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,localSignAsync任务开始",redisKey);
				localSignStart();
			} catch (Throwable e) {
				log.error("localSignAsync出错",e);
			} finally {
				log.info("释放锁:{}",redisKey);
				redisComponent.release(redisKey);
			}
		}else{
			log.info("被锁定,忽略执行, 当前线程锁: {}",redisKey);
		}
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method startLocalSign 
	 * @return void
	 * @date 2018年9月10日 下午2:32:04
	 */
	private void localSignStart() {
		List<EthTradeNonce> EthTradeNonces = ethTradeNonceMapper.findAllByState(3);
		for(EthTradeNonce ethTradeNonce: EthTradeNonces){
			localSign(ethTradeNonce);
		}
	}



	/**
	 * @author WangWei
	 * @Description 
	 * @method localSign
	 * @param ethTradeNonce 
	 * @return void
	 * @date 2018年9月10日 下午2:36:23
	 */
	private void localSign(EthTradeNonce ethTradeNonce) {
		log.info("本地进行签名 orderNo {}",ethTradeNonce.getOrderNo());
    	Credentials credentials = Credentials.create(localPriKeyMap.get(ethTradeNonce.getFromAddress()));
        RawTransaction rawTransaction  = RawTransaction.createTransaction(
        		new BigInteger(ethTradeNonce.getNonce().toString()) 
        		,ethTradeNonce.getGasPrice().multiply(Common.B1x10_9).toBigInteger()
        		,new BigInteger(Long.toString(ethTradeNonce.getGasLimit()))
        		,ethTradeNonce.getToAddress()
        		,ethTradeNonce.getValue().multiply(Common.B1x10_18).toBigInteger()
        		,ethTradeNonce.getData());//可以额外带数据
        
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String rawTransactionHex = Numeric.toHexString(signedMessage);
        ethTradeNonce.setRawTransaction(rawTransactionHex);
        ethTradeNonce.setState(4);
        ethTradeNonceMapper.save(ethTradeNonce);
	}

	@Override
	public void sendTask() {
		log.info("eth定时广播任务");
		EthSystemService thisBean = applicationContext.getBean(EthSystemService.class);
		thisBean.sendAsync();
	}

	@Async
	@Override
	public void sendAsync() {
		log.info("sendAsnyc异步任务开始");
		String redisKey = active + "_ETH_sendAsnyc_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,sendAsnyc任务开始",redisKey);
				sendStart();
			} catch (Throwable e) {
				log.error("sendAsnyc出错",e);
			} finally {
				log.info("释放锁:{}",redisKey);
				redisComponent.release(redisKey);
			}
		}else{
			log.info("被锁定,忽略执行, 当前线程锁: {}",redisKey);
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 广播交易;当有覆盖标记的时候,略过
	 * @method sendStart void
	 * @date 2018年9月13日 上午10:42:59
	 */
	private void sendStart() {
		log.info("扫描状态为4|2的交易并发送 开始");
		List<EthTradeNonce> findAllByStateIs4 = ethTradeNonceMapper.findAllByState(4);
		for(EthTradeNonce ethTradeNonce : findAllByStateIs4){
			if(ethTradeNonce.getCoverState() != null && ethTradeNonce.getCoverState() == 1){//如果覆盖标记存在,则跳过
				continue;
			}
			send(ethTradeNonce.getOrderNo());
		}
		List<EthTradeNonce> findAllByStateIs2 = ethTradeNonceMapper.findAllByState(2);
		for(EthTradeNonce ethTradeNonce : findAllByStateIs2){
			if(ethTradeNonce.getCoverState() != null && ethTradeNonce.getCoverState() == 1){//如果覆盖标记存在,则跳过
				continue;
			}
			send(ethTradeNonce.getOrderNo());
		}
	}



	/**
	 * 根据order_no 广播一笔交易
	 * 如果state==4 或2才广播
	 * 广播状态更新:
	 * 	如果返回txhash 且 原本不存在txhash,则更新hash
	 * 	如果返回error 更新 error message 和 error code
	 * 	如果交易返回错误信息包含"insufficient funds" 则删除此次交易 并return 隐患 nonce空缺
	 * 	更新广播状态state
	 * 	
	 * @throws Exception 
	 */
	private void send(String orderNo) {
		log.info("根据order_no 广播一笔交易 开始, orderNo:{}",orderNo);
		EthTradeNonce ethTradeNonce = ethTradeNonceMapper.findByorderNo(orderNo);
		
		if(ethTradeNonce == null)//交易不存在直接跳过
			return;
		
		if( StringUtil.isBlank(ethTradeNonce.getRawTransaction()) )//交易未签名直接跳过
			return;
		
		String ethSendStrategy = sysConfigClient.getSysConfigValue("eth_send_strategy");
		log.info("根据order_no 广播一笔交易 策略, orderNo:{},策略{}",orderNo,ethSendStrategy);
		String[] ethSendStrategys = ethSendStrategy.split("\\|");
		for(String strategy : ethSendStrategys){
			switch (strategy){
			case "etherscan":
				log.info("根据order_no 广播一笔交易:策略:开始, orderNo:{},策略{}",orderNo,strategy);
				JSONObject sendEtherscan = etherscanApiUtil.sendEtherscan(ethTradeNonce.getRawTransaction());
				if(sendEtherscan.getJSONObject("error") != null){
//					if(sendEtherscan.getJSONObject("error").getString("message").contains("insufficient funds")){
//						//交易失败情况,删除交易 当已orderid查询时候返回交易失败
//						ethTradeNonceMapper.delete(ethTradeNonce);
//						log.info("根据order_no 广播一笔交易:策略:交易费用不足删除该笔交易释放nonce, orderNo:{},策略{}",orderNo,strategy);
//						return;
//					}
					ethTradeNonce.setStateErrorCode(sendEtherscan.getJSONObject("error").getString("code"));
					ethTradeNonce.setStateErrorMessage(sendEtherscan.getJSONObject("error").getString("message"));
					ethTradeNonce.setState(2);
					ethTradeNonceMapper.save(ethTradeNonce);
					log.info("根据order_no 广播一笔交易 ,sendEtherscan方法 : error状态, orderNo:{},error_code : {},error_message : {}"//
							,orderNo,sendEtherscan.getJSONObject("error").getString("code"),sendEtherscan.getJSONObject("error").getString("message"));
					break;
				}
				
				if(!StringUtil.isBlank(sendEtherscan.getString("result"))){
					if(StringUtil.isBlank(ethTradeNonce.getTxhash()))
						ethTradeNonce.setTxhash(sendEtherscan.getString("result"));
					ethTradeNonceMapper.save(ethTradeNonce);
					log.info("根据order_no 广播一笔交易 ,sendEtherscan方法 成功返回txhash, orderNo:{},txhash : {}",orderNo,sendEtherscan.getString("result"));
					break;
				}
				
				break;
			case "local":
				try {
					EthSendTransaction ethSendTransaction = ethWeb3jLocolNodeUtil.sendLocalNode(ethTradeNonce.getRawTransaction());
					if(ethSendTransaction.hasError()){
//						if(ethSendTransaction.getError().getMessage().contains("insufficient funds")){
//							//交易失败情况,删除交易 当已orderid查询时候返回交易失败
//							ethTradeNonceMapper.delete(ethTradeNonce);
//							log.info("根据order_no 广播一笔交易:策略:交易费用不足删除该笔交易释放nonce, orderNo:{},策略{}",orderNo,strategy);
//							return;
//						}
						ethTradeNonce.setStateErrorCode("" + ethSendTransaction.getError().getCode());
						ethTradeNonce.setStateErrorMessage(ethSendTransaction.getError().getMessage());
						ethTradeNonce.setState(2);
						ethTradeNonceMapper.save(ethTradeNonce);
						log.info("根据order_no 广播一笔交易 ,sendLocalNode方法 : error状态, orderNo:{},error_code : {},error_message : {}"//
								,orderNo,ethSendTransaction.getError().getCode(),ethSendTransaction.getError().getMessage());
						
						break;
					}else{
						if(StringUtil.isBlank(ethTradeNonce.getTxhash()))
							ethTradeNonce.setTxhash(ethSendTransaction.getTransactionHash());
						ethTradeNonceMapper.save(ethTradeNonce);
						log.info("根据order_no 广播一笔交易 ,sendLocalNode方法 成功返回txhash, orderNo:{},txhash : {}",orderNo,ethSendTransaction.getTransactionHash());

						break;
					}
				} catch (Exception e) {
					log.error("根据order_no 广播一笔交易 ,sendLocalNode方法 出错",e);
				}
				break;
			}
		}
		log.info("根据order_no 广播一笔交易 结束, orderNo:{}",orderNo);
	}


	/**
	 * 扫描交易补全txhash
	 */
	@Async
	@Override
	public void etherscanRepairTxhashTask() {
		log.info("扫描交易补全txhash 开始");
		List<EthTradeNonce> findAllTxhashNull = ethTradeNonceMapper.findAllTxhashNull();
		for(EthTradeNonce e : findAllTxhashNull){
			if( e.getState() != 4 && e.getState() != 2 && e.getState() != 5)
				continue;
			String ensureTxHash = etherscanRepairTxhash(e);
			if(!StringUtil.isBlank(ensureTxHash)){
				e.setTxhash(ensureTxHash);
				ethTradeNonceMapper.save(e);
			}
		}
		log.info("扫描交易补全txhash 结束");
	}

	/**
	 * 补全一笔交易的txhash
	 * @param ethTradeNonce
	 * @return
	 */
	private String etherscanRepairTxhash(EthTradeNonce ethTradeNonce){
		log.info("查找一笔交易的txhash 开始,orderNo{}",ethTradeNonce.getOrderNo());
		ResponseEntity<JSONObject> postForEntity = etherscanApiUtil.txlist(ethTradeNonce.getFromAddress());
		JSONArray jsonArray = postForEntity.getBody().getJSONArray("result");
		if(jsonArray==null){
			log.error("查找一笔交易的txhash 失败 网络访问失败,orderNo{}",ethTradeNonce.getOrderNo());
			return "";
		}
		int nonceMysql = ethTradeNonce.getNonce();
		for(int i = 0 ; i < jsonArray.size() ; i++){
			int nonce = new Integer(jsonArray.getJSONObject(i).getString("nonce"));
			if(nonce == nonceMysql){
				log.info("查找一笔交易的txhash 成功,orderNo{}",ethTradeNonce.getOrderNo());
				return jsonArray.getJSONObject(i).getString("hash");
			}
			if(nonce != nonceMysql){
				log.info("查找一笔交易的txhash 失败 未找到,orderNo{}",ethTradeNonce.getOrderNo());
				continue;
			}
		}
		log.info("查找一笔交易的txhash 结束!,orderNo{}",ethTradeNonce.getOrderNo());
		return "";
	}

	/** 
	 * <p>Title: ensureTxByEtherscanTask</p>
	 * <p>Description: 扫描状态2,4的交易并确认是否有6个区块确认,可能发生的情况是最终状态为1或5</p>
	 * @see com.bithaw.zbt.service.EthSystemService#ensureTxByEtherscanTask()  
	 */
	@Async
	@Override
	public void ensureTxByEtherscanTask() {//TODO 王玮 目前只有etherscan方法 缺少本地查找方法
		log.info("确认交易最终状态,开始");
		List<EthTradeNonce> findAllByState = new ArrayList<>();
		List<EthTradeNonce> findAllByState4 = ethTradeNonceMapper.findAllByState(4);
		List<EthTradeNonce> findAllByState2 = ethTradeNonceMapper.findAllByState(2);
		findAllByState.addAll(findAllByState4);
		findAllByState.addAll(findAllByState2);
		for(EthTradeNonce ethTradeNonce : findAllByState){
			if( StringUtil.isBlank(ethTradeNonce.getTxhash()) )//没有txHash跳过
				continue;
			if( StringUtil.isBlank(ethTradeNonce.getRawTransaction()) )//没有签名跳过
				continue;
			try {
				ensureTxByEtherscan(ethTradeNonce);
			} catch (Exception e) {
				log.error("确认一笔交易最终状态失败",e); 
			}
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 查询etherscan,根据txhash查询最终状态,并更新;结果可能为1:交易成功;5交易被覆盖;
	 * @method ensureTxByEthersca
	 * @param ethTradeNonce void
	 * @date 2018年9月13日 上午10:40:26
	 */
	private void ensureTxByEtherscan(EthTradeNonce ethTradeNonce){
		log.info("etherscan确认一笔交易是否成功,如果成功修改state 开始,orderNo {}",ethTradeNonce.getOrderNo());
		Optional<ResponseEntity<JSONObject>> transactionByHash = etherscanApiUtil.getTransactionByHash(ethTradeNonce.getTxhash());
		Optional<String> blockNumber = transactionByHash
			.map(o -> o.getBody())
			.map(o -> o.getJSONObject("result"))
			.map(o -> o.getString("blockNumber"));
		if(!blockNumber.isPresent()){
			log.info("etherscan确认一笔交易是否成功 未找到交易信息,orderNo {}",ethTradeNonce.getOrderNo());
			return;
		}

		String number = blockNumber.orElseThrow(() -> new RuntimeException("确认一笔交易失败"));
		Long height = Long.parseLong(number.substring(2), 16);
		BigInteger etherscanBlockHeight = etherscanApiUtil.getEtherscanBlockHeight();
		if(etherscanBlockHeight !=null //
				&& etherscanBlockHeight.subtract(new BigInteger(height + "")).compareTo(new BigInteger("6")) > 0){
			//说明交易被6区块确认
		}else{
			//交易未确认,直接返回
			return;
		}
		
		if(ensureTxByEthersca(transactionByHash, ethTradeNonce)){
			//交易对比成功,更新状态
			ethTradeNonce.setState(1);
			ethTradeNonceMapper.save(ethTradeNonce);
			return;
		}else{
			//交易对比失败,更新状态为"覆盖"
			ethTradeNonce.setState(5);
			ethTradeNonceMapper.save(ethTradeNonce);
			return;
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 详细比对一笔交易是否全等
	 * @method ensureTxByEthersca
	 * @param transactionByHash
	 * @param ethTradeNonce
	 * @return boolean
	 * @date 2018年9月13日 上午10:12:58
	 */
	private boolean ensureTxByEthersca(Optional<ResponseEntity<JSONObject>> transactionByHash , EthTradeNonce ethTradeNonce){
		
		BigDecimal gasPrice = transactionByHash
			.map(o -> o.getBody())
			.map(o -> o.getJSONObject("result"))
			.map(o -> o.getString("gasPrice"))
			.map(o -> o.substring(2))
			.map(o -> new BigInteger(o,16))
			.map(o -> new BigDecimal(o.toString()))
			.map(o -> o.divide(Common.B1x10_9, 9, BigDecimal.ROUND_HALF_UP))
			.orElseThrow(() -> new RuntimeException("确认一笔交易失败"));
		if(!gasPrice.equals(ethTradeNonce.getGasPrice())){
			return false;
		}
		
		String to = transactionByHash
		.map(o -> o.getBody())
		.map(o -> o.getJSONObject("result"))
		.map(o -> o.getString("to"))
		.map(o -> new Address(o))
		.map(o -> o.getValue())
		.orElseThrow(() -> new RuntimeException("确认一笔交易失败"));
		String toMysql = new Address(ethTradeNonce.getToAddress()).getValue();
		if(!to.equals(toMysql)){
			return false;
		}
		
		String input = transactionByHash
		.map(o -> o.getBody())
		.map(o -> o.getJSONObject("result"))
		.map(o -> o.getString("input"))
		.map(o -> o.toLowerCase())
		.map(o -> (o.startsWith("0x") ? o : ("0x" + o) ))
		.orElseThrow(() -> new RuntimeException("确认一笔交易失败"));
		String data = ethTradeNonce.getData().toLowerCase();
		data = data.startsWith("0x") ? data : ("0x" + data) ;
		if(!input.equals(data)){
			return false;
		}
		
		//全部验证通过
		return true;
	}

	
}
