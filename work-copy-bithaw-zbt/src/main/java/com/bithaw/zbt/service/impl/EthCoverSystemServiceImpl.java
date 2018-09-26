/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午5:23:59
 * @version   V 1.0
 */
package com.bithaw.zbt.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
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
import com.bithaw.zbt.entity.EthTradeNonceCover;
import com.bithaw.zbt.feign.SysConfigClient;
import com.bithaw.zbt.mapper.EthTradeNonceCoverMapper;
import com.bithaw.zbt.mapper.EthTradeNonceMapper;
import com.bithaw.zbt.service.EthCoverSystemService;
import com.bithaw.zbt.utils.RedisComponent;
import com.bithaw.zbt.utils.eth.EthWeb3jLocolNodeUtil;
import com.bithaw.zbt.utils.eth.EtherscanApiUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 覆盖交易service
 * @author   WangWei
 * @date     2018年9月12日 下午5:23:59
 * @version  V 1.0
 */
@Slf4j
@Service
public class EthCoverSystemServiceImpl implements EthCoverSystemService {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Value("${spring.profiles.active}")
	private String active;
	
	@Autowired
	private SysConfigClient sysConfigClient;
	@Autowired
    private RedisComponent redisComponent;
	/**
	 * localPriKey : 本地数据库<公钥,私钥>
	 */
	private Map<String,String> localPriKeyMap;
	
	@Autowired
	private EthTradeNonceCoverMapper ethTradeNonceCoverMapper;
	
	@Autowired
	private EthTradeNonceMapper ethTradeNonceMapper;
	
	@Autowired
	private EtherscanApiUtil etherscanApiUtil;
	
	@Autowired
	private EthWeb3jLocolNodeUtil ethWeb3jLocolNodeUtil;
	/**
	 * @author WangWei
	 * @Description 初始化
	 * @method init void
	 * @date 2018年9月13日 上午11:43:02
	 */
	@Override
	public void init(){
		log.info("初始化 开始");
		String localPriKeys = sysConfigClient.getSysConfigValue("eth_company_address_prikey");
		localPriKeyMap = new HashMap<String, String>();
		
		for(String localPriKey : localPriKeys.split("\\|")){
			Credentials credentials = Credentials.create(localPriKey);
			localPriKeyMap.put(credentials.getAddress(), localPriKey);
		}
	}
	
	/** 
	 * <p>Title: getState</p>
	 * <p>Description: 获取一笔交易的状态-1:不存在;0:打包中;1:交易成功;-2:交易被覆盖;</p>
	 * @param coverNo
	 * @return
	 * @see com.bithaw.zbt.service.EthSystemService#getState(java.lang.String)  
	 */
	@Override
	public int getState(String coverNo) {
		log.info("获取状态开始,coverNo {}",coverNo);
		try {
			EthTradeNonceCover bean = ethTradeNonceCoverMapper.findByCoverNo(coverNo);
			if(bean == null){
				return -1;
			}
			if(bean.getState() == 5){
				return -2;
			}
			if(bean.getState() == 1){
				return 1;
			}
			return 0;
		} catch(RuntimeException e){
			log.error("获取状态 结束,coverNo {};状态 {}",coverNo,"异常",e);
			throw e;
		}finally{
			log.info("获取状态 结束,coverNo {}",coverNo);
		}
	}
	
	/** 
	 * <p>Title: setNonceTask</p>
	 * <p>Description: 设置nonce定时任务</p>
	 * @see com.bithaw.zbt.service.EthCoverSystemService#setNonceTask()  
	 */
	@Override
	public void setNonceTask() {
		EthCoverSystemService thisService = applicationContext.getBean(EthCoverSystemService.class);
		thisService.setNonceAsync();
	}
	
	@Async
	@Override
	public void setNonceAsync() {
		log.info("设置覆盖交易nonce异步执行开始");
		String redisKey = active + "_ETH_cover_setNonce_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,设置覆盖交易nonce异步执行任务开始",redisKey);
				setNonceStart();
			} catch (Throwable e) {
				log.error("设置覆盖交易nonce异步执行出错",e);
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
	 * @Description 不需要维护nonce,只需要将没有nonce的交易设置上nonce即可
	 * @method setNonceStart void
	 * @date 2018年9月13日 上午11:48:54
	 */
	private void setNonceStart() {
		List<EthTradeNonceCover> beans = ethTradeNonceCoverMapper.findAllByState(0);
		for(EthTradeNonceCover bean : beans){
			setNonce(bean);
		}
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method setNonce
	 * @param bean void
	 * @date 2018年9月13日 下午1:32:18
	 */
	private void setNonce(EthTradeNonceCover bean) {
		EthTradeNonce original = ethTradeNonceMapper.findByorderNo(bean.getOrderNo());
		if(original.getNonce()==null){
			return;
		}
		bean.setNonce(original.getNonce());
		bean.setState(3);
		ethTradeNonceCoverMapper.save(bean);
	}

	@Override
	public void localSignTask() {
		String ethLocalSignFlag = sysConfigClient.getSysConfigValue("eth_local_sign_flag");
		if( !Boolean.parseBoolean(ethLocalSignFlag) ){
			log.info("本地签名未开");
			return;
		}
		
		EthCoverSystemService thisBean = applicationContext.getBean(EthCoverSystemService.class);
		thisBean.localSignAsync();
	}

	@Async
	@Override
	public void localSignAsync() {
		log.info("覆盖交易本地签名开始");
		String redisKey = active + "_ETH_cover_localSign_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,覆盖交易本地签名任务",redisKey);
				localSignStart();
			} catch (Throwable e) {
				log.error("覆盖交易本地签名出错",e);
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
	 * @method localSignStart void
	 * @date 2018年9月13日 下午1:57:54
	 */
	private void localSignStart() {
		List<EthTradeNonceCover> beans = ethTradeNonceCoverMapper.findAllByState(3);
		for(EthTradeNonceCover bean : beans){
			localSign(bean);
		}
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method localSign
	 * @param bean void
	 * @date 2018年9月13日 下午2:00:01
	 */
	private void localSign(EthTradeNonceCover bean) {
		log.info("覆盖交易本地进行签名 coverNo {}",bean.getCoverNo());
		Credentials credentials = Credentials.create(localPriKeyMap.get(bean.getFromAddress()));
        RawTransaction rawTransaction  = RawTransaction.createTransaction(
        		new BigInteger(bean.getNonce().toString()) 
        		,bean.getGasPrice().multiply(Common.B1x10_9).toBigInteger()
        		,new BigInteger(Long.toString(bean.getGasLimit()))
        		,bean.getToAddress()
        		,bean.getValue().multiply(Common.B1x10_18).toBigInteger()
        		,bean.getData());//可以额外带数据
        
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String rawTransactionHex = Numeric.toHexString(signedMessage);
        bean.setRawTransaction(rawTransactionHex);
        bean.setState(4);
        ethTradeNonceCoverMapper.save(bean);
	}

	/** 
	 * <p>Title: sendTask</p>
	 * <p>Description: 覆盖交易定时任务</p>
	 * @see com.bithaw.zbt.service.EthCoverSystemService#sendTask()  
	 */
	@Override
	public void sendTask() {
		log.info("覆盖交易定时广播任务定时广播任务");
		EthCoverSystemService thisBean = applicationContext.getBean(EthCoverSystemService.class);
		thisBean.sendAsync();
	}

	/** 
	 * <p>Title: sendAsync</p>
	 * <p>Description: 定时广播任务异步执行</p>
	 * @see com.bithaw.zbt.service.EthCoverSystemService#sendAsync()  
	 */
	@Async
	@Override
	public void sendAsync() {
		log.info("覆盖交易异步广播任务开始");
		String redisKey = active + "_ETH_cover_sendAsnyc_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,覆盖交易异步广播任务开始",redisKey);
				sendStart();
			} catch (Throwable e) {
				log.error("覆盖交易异步广播",e);
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
	 * @method sendStart void
	 * @date 2018年9月13日 下午2:11:45
	 */
	private void sendStart() {
		log.info("扫描状态为4|2的交易并发送 开始");
		List<EthTradeNonceCover> findAllByState4 = ethTradeNonceCoverMapper.findAllByState(4);
		for(EthTradeNonceCover bean : findAllByState4){
			if(bean.getCoverState() != null && bean.getCoverState() == 1){//如果覆盖标记存在,则跳过
				continue;
			}
			send(bean.getCoverNo());
		}
		List<EthTradeNonceCover> findAllByState2 = ethTradeNonceCoverMapper.findAllByState(2);
		for(EthTradeNonceCover bean : findAllByState2){
			if(bean.getCoverState() != null && bean.getCoverState() == 1){//如果覆盖标记存在,则跳过
				continue;
			}
			send(bean.getCoverNo());
		}
	}

	/**
	 * @author WangWei
	 * @Description 只发送状态4和2的交易
	 * @method send
	 * @param coverNo void
	 * @date 2018年9月13日 下午2:15:15
	 */
	private void send(String coverNo) {
		log.info("覆盖交易 根据cover_no 广播一笔交易 开始, coverNo:{}",coverNo);
		EthTradeNonceCover bean = ethTradeNonceCoverMapper.findByCoverNo(coverNo);
		
		if(bean == null)//交易不存在直接跳过
			return;
		
		if( StringUtil.isBlank(bean.getRawTransaction()) )//交易未签名直接跳过
			return;
		
		String ethSendStrategy = sysConfigClient.getSysConfigValue("eth_send_strategy");
		log.info("覆盖交易 根据cover_no 广播一笔交易 策略, coverNo:{},策略{}",coverNo,ethSendStrategy);
		String[] ethSendStrategys = ethSendStrategy.split("\\|");
		for(String strategy : ethSendStrategys){
			switch (strategy){
			case "etherscan":
				sendByEtherscan(bean);
				break;
			case "local":
				
				break;
			}
		}
		log.info("根据cover_no 广播一笔交易 结束, coverNo:{}",coverNo);
	}
	
	private void sendByEtherscan(EthTradeNonceCover bean){
		log.info("覆盖交易 根据cover_no 广播一笔交易:开始, bean:{}",bean);
		JSONObject sendEtherscan = etherscanApiUtil.sendEtherscan(bean.getRawTransaction());
		if(sendEtherscan.getJSONObject("error") != null){
//			if(sendEtherscan.getJSONObject("error").getString("message").contains("insufficient funds")){
//				//交易失败情况,删除交易
//				ethTradeNonceCoverMapper.delete(bean);
//				log.info("根据order_no 广播一笔交易:策略:交易费用不足删除该笔交易释放nonce, bean:{}",bean);
//				return;
//			}
			bean.setStateErrorCode(sendEtherscan.getJSONObject("error").getString("code"));
			bean.setStateErrorMessage(sendEtherscan.getJSONObject("error").getString("message"));
			bean.setState(2);
			ethTradeNonceCoverMapper.save(bean);
			log.info("根据cover_no 广播一笔交易 ,sendEtherscan方法 : error状态, bean:{},error_code : {},error_message : {}"//
					,bean,sendEtherscan.getJSONObject("error").getString("code"),sendEtherscan.getJSONObject("error").getString("message"));
			return;
		}
		
		if(!StringUtil.isBlank(sendEtherscan.getString("result"))){
			if(StringUtil.isBlank(bean.getTxhash()))
				bean.setTxhash(sendEtherscan.getString("result"));
			ethTradeNonceCoverMapper.save(bean);
			log.info("根据cover_no 广播一笔交易 ,sendEtherscan方法 成功返回txhash, bean:{},txhash : {}",bean,sendEtherscan.getString("result"));
			return;
		}
	}
	
	private void sendByLocal(EthTradeNonceCover bean){
		try {
			EthSendTransaction ethSendTransaction = ethWeb3jLocolNodeUtil.sendLocalNode(bean.getRawTransaction());
			if(ethSendTransaction.hasError()){
//				if(ethSendTransaction.getError().getMessage().contains("insufficient funds")){
//					//交易失败情况,删除交易 当已coverid查询时候返回交易失败
//					ethTradeNonceCoverMapper.delete(bean);
//					log.info("根据cover_no 本地节点 广播一笔交易:交易费用不足删除该笔交易, bean:{}",bean);
//					return;
//				}
				bean.setStateErrorCode("" + ethSendTransaction.getError().getCode());
				bean.setStateErrorMessage(ethSendTransaction.getError().getMessage());
				bean.setState(2);
				ethTradeNonceCoverMapper.save(bean);
				log.info("根据cover_no 广播一笔交易 ,sendLocalNode方法 : error状态, bean:{},error_code : {},error_message : {}"//
						,bean,ethSendTransaction.getError().getCode(),ethSendTransaction.getError().getMessage());
				
				return;
			}else{
				if(StringUtil.isBlank(bean.getTxhash()))
					bean.setTxhash(ethSendTransaction.getTransactionHash());
				ethTradeNonceCoverMapper.save(bean);
				log.info("根据covr_no 广播一笔交易 ,sendLocalNode方法 成功返回txhash, bean:{},txhash : {}",bean,ethSendTransaction.getTransactionHash());

				return;
			}
		} catch (Exception e) {
			log.error("根据cover_no 广播一笔交易 ,sendLocalNode方法 出错",e);
		}
	}

	/** 
	 * <p>Title: etherscanRepairTxhashTask</p>
	 * <p>Description: 异步任务,扫描补全hash</p>
	 * @see com.bithaw.zbt.service.EthCoverSystemService#etherscanRepairTxhashTask()  
	 */
	@Async
	@Override
	public void etherscanRepairTxhashTask() {
		log.info("覆盖交易扫描交易补全txhash 开始");
		List<EthTradeNonceCover> findAllTxhashNull = ethTradeNonceCoverMapper.findAllTxhashNull();
		for(EthTradeNonceCover e : findAllTxhashNull){
			if( e.getState() != 4 && e.getState() != 2 && e.getState() != 5)
				continue;
			String ensureTxHash = etherscanRepairTxhash(e);
			if(!StringUtil.isBlank(ensureTxHash)){
				e.setTxhash(ensureTxHash);
				ethTradeNonceCoverMapper.save(e);
			}
		}
		log.info("覆盖交易扫描交易补全txhash 结束");
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method etherscanRepairTxhash
	 * @param e
	 * @return String
	 * @date 2018年9月13日 下午3:14:44
	 */
	private String etherscanRepairTxhash(EthTradeNonceCover e) {
		log.info("查找一笔交易的txhash 开始,coverNo{}",e.getCoverNo());
		ResponseEntity<JSONObject> postForEntity = etherscanApiUtil.txlist(e.getFromAddress());
		JSONArray jsonArray = postForEntity.getBody().getJSONArray("result");
		if(jsonArray == null){
			log.error("查找一笔交易的txhash 失败 网络访问失败,coverNo{}",e.getCoverNo());
			return "";
		}
		int nonceMysql = e.getNonce();
		for(int i = 0 ; i < jsonArray.size() ; i++){
			int nonce = new Integer(jsonArray.getJSONObject(i).getString("nonce"));
			if(nonce == nonceMysql){
				log.info("查找一笔交易的txhash 成功,coverNo{}",e.getCoverNo());
				return jsonArray.getJSONObject(i).getString("hash");
			}
			if(nonce != nonceMysql){
				log.info("查找一笔交易的txhash 失败 未找到,coverNo{}",e.getCoverNo());
				continue;
			}
		}
		log.info("查找一笔交易的txhash 结束!,orderNo{}",e.getCoverNo());
		return "";
	}

	/** 
	 * <p>Title: ensureTxByEtherscanTask</p>
	 * <p>Description: 扫描状态2,4的交易并确认是否有6个区块确认,可能发生的情况是最终状态为1或5</p>
	 * @see com.bithaw.zbt.service.EthCoverSystemService#ensureTxByEtherscanTask()  
	 */
	@Async
	@Override
	public void ensureTxByEtherscanTask() {
		log.info("覆盖交易确认交易最终状态,开始");
		List<EthTradeNonceCover> findAllByState = new ArrayList<>();
		List<EthTradeNonceCover> findAllByState4 = ethTradeNonceCoverMapper.findAllByState(4);
		List<EthTradeNonceCover> findAllByState2 = ethTradeNonceCoverMapper.findAllByState(2);
		findAllByState.addAll(findAllByState4);
		findAllByState.addAll(findAllByState2);
		for(EthTradeNonceCover bean : findAllByState){
			if( StringUtil.isBlank(bean.getTxhash()) )//没有txHash跳过
				continue;
			if( StringUtil.isBlank(bean.getRawTransaction()) )//没有签名跳过
				continue;
			try {
				ensureTxByEtherscan(bean);
			} catch (Exception e) {
				log.error("覆盖交易确认一笔交易最终状态失败",e); 
			}
		}
	}

	/**
	 * @author WangWei
	 * @Description 查询etherscan,根据txhash查询最终状态,并更新;结果可能为1:交易成功;5交易被覆盖;
	 * @method ensureTxByEtherscan
	 * @param bean void
	 * @date 2018年9月13日 下午3:33:17
	 */
	private void ensureTxByEtherscan(EthTradeNonceCover bean) {
		log.info("覆盖交易 etherscan确认一笔交易是否成功,如果成功修改state 开始,bean {}",bean);
		Optional<ResponseEntity<JSONObject>> transactionByHash = etherscanApiUtil.getTransactionByHash(bean.getTxhash());
		Optional<String> blockNumber = transactionByHash
			.map(o -> o.getBody())
			.map(o -> o.getJSONObject("result"))
			.map(o -> o.getString("blockNumber"));
		if(!blockNumber.isPresent()){
			log.info("覆盖交易 etherscan确认一笔交易是否成功 未找到交易信息,bean {}",bean);
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
		
		if(ensureTxByEthersca(transactionByHash, bean)){
			//交易对比成功,更新状态
			bean.setState(1);
			ethTradeNonceCoverMapper.save(bean);
			return;
		}else{
			//交易对比失败,更新状态为"覆盖"
			bean.setState(5);
			ethTradeNonceCoverMapper.save(bean);
			return;
		}
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method ensureTxByEthersca
	 * @param transactionByHash
	 * @param bean
	 * @return boolean
	 * @date 2018年9月13日 下午3:38:06
	 */
	private boolean ensureTxByEthersca(Optional<ResponseEntity<JSONObject>> transactionByHash, EthTradeNonceCover bean) {
		BigDecimal gasPrice = transactionByHash
				.map(o -> o.getBody())
				.map(o -> o.getJSONObject("result"))
				.map(o -> o.getString("gasPrice"))
				.map(o -> o.substring(2))
				.map(o -> new BigInteger(o,16))
				.map(o -> new BigDecimal(o.toString()))
				.map(o -> o.divide(Common.B1x10_9, 9, BigDecimal.ROUND_HALF_UP))
				.orElseThrow(() -> new RuntimeException("确认一笔交易失败"));
			if(!gasPrice.equals(bean.getGasPrice())){
				return false;
			}
			
			String to = transactionByHash
			.map(o -> o.getBody())
			.map(o -> o.getJSONObject("result"))
			.map(o -> o.getString("to"))
			.map(o -> new Address(o))
			.map(o -> o.getValue())
			.orElseThrow(() -> new RuntimeException("确认一笔交易失败"));
			String toMysql = new Address(bean.getToAddress()).getValue();
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
			String data = bean.getData().toLowerCase();
			data = data.startsWith("0x") ? data : ("0x" + data) ;
			if(!input.equals(data)){
				return false;
			}
			
			//全部验证通过
			return true;
	}
	
	/** 
	 * <p>Title: finalStateTask</p>
	 * <p>Description: 最终状态确认任务</p>
	 * @see com.bithaw.zbt.service.EthCoverSystemService#finalStateTask()  
	 */
	@Override
	public void finalStateTask(){
		log.info("最终状态确认任务");
		EthCoverSystemService thisBean = applicationContext.getBean(EthCoverSystemService.class);
		thisBean.finalStateAsync();
	}
	
	/** 
	 * <p>Title: finalStateAsync</p>
	 * <p>Description: 最终状态确认异步方法</p>
	 * @see com.bithaw.zbt.service.EthCoverSystemService#finalStateAsync()  
	 */
	@Async
	@Override
	public void finalStateAsync(){
		log.info("最终状态异步方法");
		String redisKey = active + "_ETH_cover_finalState_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,最终状态异步方法任务",redisKey);
				finalStateStart();
			} catch (Throwable e) {
				log.error("最终状态异步方法出错",e);
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
	 * @Description 最终状态确认开始
	 * @method finalStateStart void
	 * @date 2018年9月13日 下午4:19:37
	 */
	private void finalStateStart() {
		List<EthTradeNonceCover> allBeans = ethTradeNonceCoverMapper.findAllFinalStateNull();
		Map<String,List<EthTradeNonceCover>> beanGroup = finalStateGroup(allBeans);//按照order_no分组
		for(String orderNo : beanGroup.keySet()){
			finalState(orderNo,beanGroup.get(orderNo));
		}
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method finalStateGetOrderList
	 * @param allBeans
	 * @return List<List<EthTradeNonceCover>>
	 * @date 2018年9月13日 下午4:25:24
	 */
	private Map<String,List<EthTradeNonceCover>> finalStateGroup(List<EthTradeNonceCover> allBeans) {
		
		Map<String,List<EthTradeNonceCover>> returnMap = new HashMap<String,List<EthTradeNonceCover>>();
		
		for(EthTradeNonceCover bean : allBeans){
			returnMap.put(bean.getOrderNo(), new ArrayList<EthTradeNonceCover>());
		}
		
		for(EthTradeNonceCover bean : allBeans){
			returnMap.get(bean.getOrderNo()).add(bean);
		}
		
		return returnMap;
	}
	
	/**
	 * @author WangWei
	 * @Description 确认一组交易的最终态
	 * @method finalState
	 * @param orderNo
	 * @param list void
	 * @date 2018年9月13日 下午4:36:54
	 */
	private void finalState(String orderNo, List<EthTradeNonceCover> beans) {
		EthTradeNonce originalBean = ethTradeNonceMapper.findByorderNo(orderNo);
		
		boolean flag = false;//是否有交易被确认标识
		
		if(originalBean.getState() != 4 
				&& originalBean.getState() != 2
				&& originalBean.getState() != 1
				&& originalBean.getState() != 5){
			log.info("order:{};原始交易状态非最终态所需,略过",orderNo);
			return;
		}
		if(originalBean.getState() == 1){
			log.info("order:{};原始交易成功",orderNo);
			flag = true;
		}
		for(EthTradeNonceCover bean : beans){
			if(bean.getState() != 4 
					&& bean.getState() != 2
					&& bean.getState() != 1
					&& bean.getState() != 5){
				log.info("覆盖交易状态非最终态所需,略过");
				return;
			}
			if(bean.getState() == 1){
				log.info("order:{};覆盖交易成功;coverNo",orderNo,bean.getCoverNo());
				flag = true;
			}
		}
		
		if(!flag){
			log.info("orderNo:{};覆盖交易组中没有交易成功",orderNo);
			return;
		}
		
		if(originalBean.getState() == 1){
			log.info("覆盖交易 原始交易成功,设置覆盖交易状态为5,同时标记最终态");
			for(EthTradeNonceCover bean : beans){
				bean.setState(5);
				bean.setFinalState(1);
				ethTradeNonceCoverMapper.save(bean);
			}
			return;
		}
		
		if(originalBean.getState() != 1){
			log.info("覆盖成功,设置原始交易状态为5,剩余交易状态为5,同时标记最终态");
			originalBean.setState(5);
			ethTradeNonceMapper.save(originalBean);
			
			for(EthTradeNonceCover bean : beans){
				if(bean.getState() == 1){
					
				}else{
					bean.setState(5);
				}
				bean.setFinalState(1);
				ethTradeNonceCoverMapper.save(bean);
			}
			return;
		}
	}
}
