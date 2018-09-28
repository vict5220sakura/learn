/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午5:29:12
 * @version   V 1.0
 */
package com.bithaw.btc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.comtroller.api.BtcTradeController.JSONBuilder;
import com.bithaw.btc.entity.BtcTrade;
import com.bithaw.btc.entity.BtcTradeInput;
import com.bithaw.btc.entity.BtcTradeOutput;
import com.bithaw.btc.entity.UTXO;
import com.bithaw.btc.exception.NotEnoughTempMoney;
import com.bithaw.btc.exception.OutlineSignAmountException;
import com.bithaw.btc.feign.SysCoinfigClient;
import com.bithaw.btc.mapper.BtcTradeInputMapper;
import com.bithaw.btc.mapper.BtcTradeMapper;
import com.bithaw.btc.mapper.BtcTradeOutputMapper;
import com.bithaw.btc.service.BtcSystemService;
import com.bithaw.btc.utils.BtcOutlineUtils;
import com.bithaw.btc.utils.BtcOutlineUtils.Input;
import com.bithaw.btc.utils.BtcOutlineUtils.Output;
import com.bithaw.btc.utils.BitRestUtils;
import com.bithaw.btc.utils.BitRpcUtils;
import com.bithaw.btc.utils.BitcoinjUtil;
import com.bithaw.btc.utils.BlockchainApiUtils;
import com.bithaw.btc.utils.BtcComApiUtils;
import com.bithaw.btc.utils.RedisComponent;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 比特币交易服务类
 * @author   WangWei
 * @date     2018年9月5日 下午5:29:12
 * @version  V 1.0
 */
@Slf4j
@Service
public class BtcSystemServiceImpl implements BtcSystemService{
	
	@Autowired
	private BitcoinjUtil bitcoinjUtil;
	
	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	
	@Autowired
	private RedisComponent redisComponent;
	
	@Value("${spring.profiles.active}")
	private String active;
	
	@Autowired
	private BlockchainApiUtils blockchainApiUtils;
	
	@Autowired
	private BitRestUtils bitRestUtils;
	
	@Autowired
	private BtcTradeMapper btcTradeMapper;
	
	@Autowired
	private BtcTradeInputMapper btcTradeInputMapper;
	
	@Autowired
	private BtcTradeOutputMapper btcTradeOutputMapper;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private BtcOutlineUtils btcOutlineUtils;
	
	@Autowired
	private BtcComApiUtils btcComApiUtils;
	
	/**
	 * JSONObject构造器
	 * @author WangWei
	 * @date: 2018年8月24日 上午9:52:30
	 * @version: v1.0.0
	 * @Description:构造者模式构造JSONObject
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
	 * <p>Title: createBtcAddress</p>
	 * <p>Description: </p>
	 * @return 私钥|公钥(base58格式)
	 * @see com.bithaw.btc.service.BtcSystemService#createBtcAddress()  
	 */
	@Override
	public String createBtcAddress() {
		log.info("创建比特币公钥私钥对");
		String newAddress = bitcoinjUtil.newAddress();
		return newAddress;
	}

	@Override
	public void checkNode() {
		log.info("检查节点");
		Optional<Integer> connectionCount = bitRestUtils.getConnectionCount();
		
		if( !connectionCount.isPresent() || connectionCount.get() < 1 ){
			log.error("检查节点完毕,节点链接异常");
			sysCoinfigClient.setSysConfigValue("btc_node_state", "fail");
		}
		
		log.info("检查节点完毕,节点链接正常");
		sysCoinfigClient.setSysConfigValue("btc_node_state", "ok");
	}

	/** 
	 * <p>Title: buildTrade 构建交易任务</p>
	 * <p>Description:  </p>
	 * @see com.bithaw.btc.service.BtcSystemService#buildTrade()  
	 */
	@Override
	public void buildTradeTask() {
		log.info("构建交易输入输出任务执行");
		BtcSystemService thisBean = applicationContext.getBean(this.getClass());
		thisBean.buildTradeAsync();//执行异步任务
		log.info("构建交易输入输出任务执行中...,本方法退出");
	}
	
	/** 
	 * <p>Title: buildTradeTask 构建交易任务异步执行</p>
	 * <p>Description: redis锁保证串行任务执行</p>
	 * @see com.bithaw.btc.service.BtcTradeAsyncTaskService#buildTradeTask()  
	 */
	@Async
	@Override
	public void buildTradeAsync(){
		log.info("buildTrade异步任务开始");
		String redisKey = active + "_BTC_BUILDTRADE_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			log.info("加锁 成功,当前锁: {} ,构建交易任务开始",redisKey);
			try {
				startBuildTrade();
			} catch (Throwable e) {
				log.error("构建交易出错",e);
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
	 * @Description 开始构建任务,查找状态为0的交易
	 * @method startBuildTradeTask 
	 * @return void
	 * @date 2018年9月6日 下午4:09:56
	 */
	private void startBuildTrade() {
		log.info("开始构建交易");
		//查找state为0的多笔交易
		List<BtcTrade> btcTrades = btcTradeMapper.findByState(0);
		for(BtcTrade btcTrade : btcTrades){
			try {
				buildTradeOnce(btcTrade);
			} catch (NotEnoughTempMoney e) {
				log.info("构建失败,可能是零钱不足,稍后重试!");
			}
		}
	}

	/**
	 * @author WangWei
	 * @Description 构建一笔交易 
	 * @method buildTradeOnce
	 * @param btcTrade 
	 * @return void
	 * @throws Exception 
	 * @date 2018年9月6日 下午2:13:15
	 */
	private void buildTradeOnce(BtcTrade btcTrade){
		BigDecimal amount = btcTrade.getAmount();
		BigDecimal fees = btcTrade.getFees();
		BigDecimal allAmount = amount.add(fees);//计算转账的总金额
		
		String btcCompanyAddresses = sysCoinfigClient.getSysConfigValue("btc_company_address");//获取公司比特币账号
		
		String btcGetutxoMethods = sysCoinfigClient.getSysConfigValue("btc_getutxo_method");//获取零钱策略
		
		//查找全部零钱
		Optional<List<UTXO>> utxos =  findAllUTXOByAllMethod(btcCompanyAddresses,btcGetutxoMethods);
		
		//过滤掉已经使用的零钱
		utxos = filterUsed(utxos);
		
		//判断零钱是否够用
		if(!enoughBtc(allAmount,utxos)){
			throw new NotEnoughTempMoney("公司比特账号余额不足,请稍后重试");
		}
		
		//剩余零钱过滤掉不需要的零钱
		utxos = filterNeedless(allAmount,utxos);
		
		//计算找零
		BigDecimal changeMoney = changeMoney(utxos,allAmount);
		
		//构建输入与输出,存入数据库
		BtcSystemService thisBean = applicationContext.getBean(this.getClass());
		thisBean.saveInOutDatebase(btcTrade,utxos,btcCompanyAddresses,changeMoney);
		
		//调用一次 签名交易
		signOnce(btcTrade);
		//调用一次 广播交易
		//sendRawtransactionOnce(btcTrade);
	}


	private Optional<List<UTXO>> findAllUTXOByAllMethod(String btcCompanyAddresses,String btcGetutxoMethod) {
		String[] methods = btcGetutxoMethod.split("\\|");
		for(String method : methods){
			try {
				Optional<List<UTXO>> utxos = findAllUTXOByOneMethod(btcCompanyAddresses, method);
				if(!utxos.isPresent()){
					log.warn("{}方式查找零钱失败,请注意",method);
					continue;
				}
				log.info("{}方式查找零钱成功",method);
				return utxos;
			} catch (Exception e) {
				log.warn("{}方式查找零钱失败,请注意",method);
				continue;
			}
		}
		log.error("全部方式查找零钱失败,请注意");
		throw new RuntimeException("查找零钱失败");
	}


	/**
	 * @author WangWei
	 * @Description 查找全部公司账号的全部零钱
	 * @method findAllUTXO
	 * @param address 公司比特币账号地址
	 * @return Optional<List<UTXO>>
	 * @date 2018年9月6日 下午2:18:51
	 */
	private Optional<List<UTXO>> findAllUTXOByOneMethod(String btcCompanyAddresses,String btcGetutxoMethod) {
		log.info("查找全部公司账号的全部零钱");
		String[] addresses = Optional.ofNullable(btcCompanyAddresses)
			.map(p -> p.split("\\|"))
			.orElseThrow(() -> new NotEnoughTempMoney("公司比特币账号不存在"));
		ArrayList<UTXO> returnList = new ArrayList<UTXO>();
		for(String address : addresses){
			Optional<List<UTXO>> utxOs = Optional.ofNullable(new ArrayList<UTXO>()) ;
			switch (btcGetutxoMethod) {
			case "blockchain":
				utxOs = blockchainApiUtils.getUTXOs(address);
				break;
			case "local":
				utxOs = bitRestUtils.getUtxosByAddress(address);
				break;
			}
			returnList.addAll(utxOs.orElseGet(() -> new ArrayList<UTXO>()));
		}
		
		return Optional.ofNullable(returnList);
	}
	
	/**
	 * @author WangWei
	 * @Description 过滤掉已经使用的零钱
	 * @method filter
	 * @param utxos
	 * @return Optional<List<UTXO>>
	 * @date 2018年9月6日 下午2:31:58
	 */
	private Optional<List<UTXO>> filterUsed(Optional<List<UTXO>> utxos) {
		log.info("过滤掉已经使用的零钱");
		ArrayList<UTXO> returnList = new ArrayList<UTXO>();
		for(UTXO utxo : utxos.orElseThrow(() -> new NotEnoughTempMoney("公司比特账号余额不足,请稍后重试"))){
			String txId = utxo.getTxid();
			int txIndex = Integer.parseInt(utxo.getIndex());
			Optional<BtcTradeInput> btcTradeInput = Optional.ofNullable(btcTradeInputMapper.findByTxIdAndTxIndex(txId,txIndex));
			if(btcTradeInput.isPresent()){
				continue;//数据库已存在,过滤掉
			}
			returnList.add(utxo);
		}
		return Optional.ofNullable(returnList);
	}
	
	/**
	 * @author WangWei
	 * @param btcTrade 
	 * @Description 判断找到的零钱是否够用
	 * @method enoughBtc
	 * @param utxos
	 * @return boolean true:够用;false:不够用
	 * @date 2018年9月6日 下午2:42:40
	 */
	private boolean enoughBtc(BigDecimal allAmount, Optional<List<UTXO>> utxos) {
		log.info("判断金额是否够用");
		List<UTXO> utxoList = utxos.orElseThrow(() -> new NotEnoughTempMoney("公司比特账号余额不足,请稍后重试"));
		BigDecimal value = BigDecimal.ZERO;
		for(UTXO utxo : utxoList){
			value = value.add(new BigDecimal(utxo.getValue()));
		}
		
		if(value.compareTo(allAmount) >= 0){
			return true;
		}
		return false;
	}

	/**
	 * @author WangWei
	 * @param btcTrade 
	 * @Description 过滤掉不需要的零钱
	 * @method filterNeedless
	 * @param utxos
	 * @return Optional<List<UTXO>>
	 * @date 2018年9月6日 下午2:52:19
	 */
	private Optional<List<UTXO>> filterNeedless(BigDecimal allAmount, Optional<List<UTXO>> utxos) {
		log.info("过滤掉不需要的零钱");
		List<UTXO> utxoList = utxos.orElseThrow(() -> new NotEnoughTempMoney("公司比特账号余额不足,请稍后重试"));
		ArrayList<UTXO> returnList = new ArrayList<UTXO>();
		BigDecimal value = BigDecimal.ZERO;
		for(UTXO utxo : utxoList){
			value = value.add(new BigDecimal(utxo.getValue()));
			returnList.add(utxo);
			if(value.compareTo(allAmount) >= 0){
				break;
			}
		}
		return Optional.ofNullable(returnList);
	}
	
	/**
	 * @author WangWei
	 * @Description 计算找零
	 * @method changeMoney
	 * @param utxos
	 * @param allAmount
	 * @return BigDecimal
	 * @date 2018年9月6日 下午3:33:00
	 */
	private BigDecimal changeMoney(Optional<List<UTXO>> utxos, BigDecimal allAmount) {
		log.info("计算找零");
		BigDecimal value = BigDecimal.ZERO;
		for(UTXO utxo : utxos.orElseThrow(() -> new NotEnoughTempMoney("公司比特账号余额不足,请稍后重试"))){
			value = value.add(new BigDecimal(utxo.getValue()));
		}
		return value.subtract(allAmount);
	}
	
	/**
	 * @author WangWei
	 * @Description 构建输入输出存入数据库
	 * @method buildInOut
	 * @param btcTrade
	 * @param utxos 
	 * @param btcCompanyAddresses 公司地址,也是找零地址
	 * @return void
	 * @throws Exception 
	 * @date 2018年9月6日 下午3:04:14
	 */
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveInOutDatebase(BtcTrade btcTrade, Optional<List<UTXO>> utxos,String btcCompanyAddresses,BigDecimal changeMoney) {
		//构建输入
		for(UTXO utxo : utxos.orElseThrow(() -> new NotEnoughTempMoney("公司比特账号余额不足,请稍后重试"))){
			BtcTradeInput btcTradeInput = new BtcTradeInput().builder()
				.address(utxo.getAddress())
				.amount(new BigDecimal(utxo.getValue()))
				.confirmations(Integer.parseInt(utxo.getConfirmations()))
				.createTime(new Date())
				.scriptPubkey(utxo.getScriptPubKey())
				.txId(utxo.getTxid())
				.txIndex(Integer.parseInt(utxo.getIndex()))
				.uuid(btcTrade.getUuid())
				.build();
			btcTradeInputMapper.save(btcTradeInput);
		}
		
		//构建转账输出
		BtcTradeOutput tradeOutput = new BtcTradeOutput().builder()
			.address(btcTrade.getToAddress())
			.amount(btcTrade.getAmount())
			.createTime(new Date())
			.uuid(btcTrade.getUuid())
			.build();
		btcTradeOutputMapper.save(tradeOutput);
		
		//构建找零输出
		BtcTradeOutput changeOutput = new BtcTradeOutput().builder()
				.address(btcCompanyAddresses.split("\\|")[0])
				.amount(changeMoney)
				.createTime(new Date())
				.uuid(btcTrade.getUuid())
				.build();
		btcTradeOutputMapper.save(changeOutput);
		//更改状态
		btcTrade.setState(3);
		btcTrade.setUpdateTime(new Date());
		btcTradeMapper.save(btcTrade);
	}

	/** 
	 * <p>Title: signTask</p>
	 * <p>Description: </p>
	 * @see com.bithaw.btc.service.BtcSystemService#signTask()  
	 */
	@Override
	public void signTask() {
		log.info("构建签名任务开始");
		BtcSystemService thisBean = applicationContext.getBean(this.getClass());
		thisBean.signAsync();//执行异步任务
		log.info("构建签名任务执行中...,本方法退出");
	}
	
	/** 
	 * <p>Title: sign</p>
	 * <p>Description: </p>
	 * @see com.bithaw.btc.service.BtcSystemService#sign()  
	 */
	@Async
	@Override
	public void signAsync(){
		log.info("signAsync异步执行开始");
		String redisKey = active + "_BTC_SIGN_ASYNC_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,签名任务开始",redisKey);
				startSign();
			} catch (Throwable e) {
				log.error("签名任务出错",e);
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
	 * @Description 开始签名,查找state=3的交易
	 * @method startSign 
	 * @return void
	 * @date 2018年9月6日 下午6:39:32
	 */
	private void startSign() {
		List<BtcTrade> btcTrades = btcTradeMapper.findByState(3);
		for(BtcTrade btcTrade : btcTrades){
			signOnce(btcTrade);
		}
	}
	
	private void signOnce(BtcTrade btcTrade){
		ArrayList<Input> inputList = new ArrayList<BtcOutlineUtils.Input>();
		ArrayList<Output> outputList = new ArrayList<BtcOutlineUtils.Output>();
		
		String[] addresses = sysCoinfigClient.getSysConfigValue("btc_company_address").split("\\|");
		List<String> addressesList = Arrays.asList(addresses);
		String[] addressesPrikeys = sysCoinfigClient.getSysConfigValue("btc_company_address_prikey").split("\\|");
		
		List<BtcTradeInput> btcTradeInputs = btcTradeInputMapper.findByUuid(btcTrade.getUuid());
		for(BtcTradeInput btcTradeInput : btcTradeInputs){
			int addressKeyIndex = addressesList.indexOf(btcTradeInput.getAddress());
			
			Input input = new Input.Builder()
				.setAddress(btcTradeInput.getAddress())
				.setAmount(btcTradeInput.getAmount().toPlainString())
				.setConfirmations(String.valueOf(btcTradeInput.getConfirmations()))
				.setInputIndex(Long.parseLong(String.valueOf(btcTradeInput.getTxIndex())))
				.setInputTxid(btcTradeInput.getTxId())
				.setScriptPubKey(btcTradeInput.getScriptPubkey())
				.setPrivateKeyHex(addressesPrikeys[addressKeyIndex])//16进制私钥
				.build();
			inputList.add(input);
		}
		
		List<BtcTradeOutput> btcTradeOutputs = btcTradeOutputMapper.findByUuid(btcTrade.getUuid());
		for(BtcTradeOutput btcTradeOutput : btcTradeOutputs){
			Output output = new Output.Builder()
				.setAddress(btcTradeOutput.getAddress())
				.setAmount(btcTradeOutput.getAmount().toPlainString())
				.build();
			outputList.add(output);
		}
		
		try {
			String rawTransaction = btcOutlineUtils.outLineSign(inputList, outputList, btcTrade.getFees().toPlainString());
			btcTrade.setRawTransaction(rawTransaction);
			btcTrade.setState(4);
			btcTrade.setUpdateTime(new Date());
			btcTradeMapper.save(btcTrade);
		} catch (OutlineSignAmountException e) {
			log.error("签名失败,请检查",e);
		}
	}

	/** 
	 * <p>Title: sendRawtransactionTask</p>
	 * <p>Description: </p>
	 * @see com.bithaw.btc.service.BtcSystemService#sendRawtransactionTask()  
	 */
	@Override
	public void sendRawtransactionTask() {
		log.info("广播任务开始");
		BtcSystemService thisBean = applicationContext.getBean(this.getClass());
		thisBean.sendRawtransactionAsync();//执行异步任务
		log.info("广播任务开始异步执行中...,本方法退出");
	}

	/** 
	 * <p>Title: sendRawtransactionAsync</p>
	 * <p>Description: </p>
	 * @see com.bithaw.btc.service.BtcSystemService#sendRawtransactionAsync()  
	 */
	@Override
	public void sendRawtransactionAsync() {
		log.info("sendRawtransactionAsync异步执行开始");
		String redisKey = active + "_BTC_SENDRAWTRANSACTION_ASYNC_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,广播任务开始",redisKey);
				startSendRawtransaction();
			} catch (Throwable e) {
				log.error("广播任务出错",e);
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
	 * @Description 开始广播任务
	 * @method startSendRawtransaction 
	 * @return void
	 * @date 2018年9月6日 下午7:48:33
	 */
	private void startSendRawtransaction(){
		//找到状态为4的交易
		List<BtcTrade> btcTrades = btcTradeMapper.findByState(4);
		for(BtcTrade btcTrade : btcTrades){
			sendRawtransactionOnce(btcTrade);
		}
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method sendRawtransactionOnce
	 * @param btcTrade 
	 * @return void
	 * @date 2018年9月6日 下午7:50:21
	 */
	private void sendRawtransactionOnce(BtcTrade btcTrade) {
		Optional<String> txhash = bitRestUtils.sendRawtransaction(btcTrade.getRawTransaction());
		try {
			String txhashStr = txhash.orElseThrow(() -> new Exception("广播失败"));
			btcTrade.setTxhash(txhashStr);
			btcTrade.setState(5);
			btcTrade.setUpdateTime(new Date());
			btcTradeMapper.save(btcTrade);
		} catch (Exception e) {
			log.error("广播失败",e);
		}
	}

	/** 
	 * <p>Title: ensureTransactionTask</p>
	 * <p>Description: </p>
	 * @see com.bithaw.btc.service.BtcSystemService#ensureTransactionTask()  
	 */
	@Override
	public void ensureTransactionTask() {
		log.info("确认交易任务开始");
		BtcSystemService thisBean = applicationContext.getBean(this.getClass());
		thisBean.ensureTransactionAsync();//执行异步任务
		log.info("确认交易开始异步执行中,本方法退出");
	}

	/** 
	 * <p>Title: ensureTransactionAsync</p>
	 * <p>Description: 确认交易异步任务</p>
	 * @see com.bithaw.btc.service.BtcSystemService#ensureTransactionAsync()  
	 */
	@Async
	@Override
	public void ensureTransactionAsync() {
		log.info("ensureTransactionAsync异步执行开始");
		String redisKey = active + "_BTC_ENSURETRANSACTION_ASYNC_KEY";
		if(redisComponent.acquire(redisKey, 10L * 60L)){
			try {
				log.info("加锁 成功,当前锁: {} ,ensureTransactionAsync任务开始",redisKey);
				startEnsureTransaction();
			} catch (Throwable e) {
				log.error("ensureTransactionAsync任务出错",e);
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
	 * @Description 查询state=5的交易确认是否有12个区块确认
	 * @method startEnsureTransaction 
	 * @return void
	 * @date 2018年9月7日 上午9:30:01
	 */
	private void startEnsureTransaction(){
		log.info("查询state=5交易确认是否12个确认区块");
		List<BtcTrade> btcTrades = btcTradeMapper.findByState(5);
		for(BtcTrade btcTrade : btcTrades){
			ensureTransactionOnce(btcTrade);
		}
	}

	/**
	 * @author WangWei
	 * @Description 根据策略,确认一笔交易是否成功,如果有多个策略,则必须全部通过
	 * @method ensureTransactionOnce
	 * @param btcTrade 
	 * @return void
	 * @date 2018年9月7日 上午9:32:09
	 */
	private void ensureTransactionOnce(BtcTrade btcTrade) {
		String[] btcEncureTxStrategys = sysCoinfigClient.getSysConfigValue("btc_encure_tx_strategy").split("\\|");
		boolean ensureFlag = ensureTransactionOnce(btcTrade,btcEncureTxStrategys);
		if(ensureFlag){
			btcTrade.setState(1);
			btcTrade.setUpdateTime(new Date());
			btcTradeMapper.save(btcTrade);
		}
	}

	/**
	 * @author WangWei
	 * @Description 遍历每一种策略,都通过12区块认证,则返回真
	 * @method ensureTransactionOnce
	 * @param btcTrade
	 * @param btcEncureTxStrategys
	 * @return boolean
	 * @date 2018年9月7日 上午9:43:16
	 */
	private boolean ensureTransactionOnce(BtcTrade btcTrade, String[] btcEncureTxStrategys) {
		for(String btcEncureTxStrategy : btcEncureTxStrategys){
			if(!ensureTransactionOnce(btcTrade,btcEncureTxStrategy)){
				return false;
			}
		}
		return true;
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method ensureTransactionOnce
	 * @param btcTrade
	 * @param btcEncureTxStrategy
	 * @return boolean
	 * @date 2018年9月7日 上午9:44:29
	 */
	private boolean ensureTransactionOnce(BtcTrade btcTrade, String btcEncureTxStrategy) {
		switch (btcEncureTxStrategy) {
		case "local":
			Optional<Long> confirmationsBtTxHash = bitRestUtils.getConfirmationsBtTxHash(btcTrade.getTxhash());
			Optional<Long> filter = confirmationsBtTxHash
				.filter(p -> (p.longValue() >= 12L));
			if(filter.isPresent()){
				log.info("本地节点验证一笔交易是否成功通过");
				return true;
			}else{
				log.info("本地节点验证一笔交易是否成功不通过");
				return false;
			}
		case "blockchain":
			Optional<Long> confirmationsBtTxHash2 = blockchainApiUtils.getConfirmationsBtTxHash(btcTrade.getTxhash());
			Optional<Long> filter2 = confirmationsBtTxHash2
					.filter(p -> (p.longValue() >= 12L));
				if(filter2.isPresent()){
					log.info("blockchain节点验证一笔交易是否成功通过");
					return true;
				}else{
					log.info("blockchain节点验证一笔交易是否成功不通过");
					return false;
				}
		case "btc":
			Optional<Long> confirmationsBtTxHash3 = btcComApiUtils.getConfirmationsBtTxHash(btcTrade.getTxhash());
			Optional<Long> filter3 = confirmationsBtTxHash3
					.filter(p -> (p.longValue() >= 12L));
				if(filter3.isPresent()){
					log.info("btc.com节点验证一笔交易是否成功通过");
					return true;
				}else{
					log.info("btc.com节点验证一笔交易是否成功不通过");
					return false;
				}
		default:
			log.error("验证一笔交易是否成功,验证节点没有策略!");
			return false;
		}
	}

	/** 
	 * <p>Title: getTradeStatus</p>
	 * <p>Description: 获取状态</p>
	 * @param uuid
	 * @return
	 * @see com.bithaw.btc.service.BtcSystemService#getTradeStatus(java.lang.String)  
	 */
	@Override
	public String getTradeStatus(String uuid) {
		try {
			BtcTrade findByUuid = btcTradeMapper.findByUuid(uuid);
			if(findByUuid == null){
				return "FAIL";
			}
			
			if(findByUuid.getState() != 1){
				return "TRADING";
			}
			
			if(findByUuid.getState() == 1){
				return "SUCCESS";
			}
			
			return "ERROR";
		} catch (Exception e) {
			log.error("比特币系统查询状态出错,uuid{}",uuid);
			return "ERROR";
		}
	}

	@Override
	public String getTradeInfo(String uuid) {
		try {
			BtcTrade findByUuid = btcTradeMapper.findByUuid(uuid);
			if(findByUuid == null){
				return new JSONBuilder().put("code", "0").put("message","not find").build().toJSONString();
			}
			
			Optional<Long> blockHeight = blockchainApiUtils.getBlockHeightByTxhash(findByUuid.getTxhash());
			Long height = blockHeight.orElseThrow(() -> new RuntimeException("没有交易确认高度"));
			
			return new JSONBuilder().put("code", "1").put("message","success").put("fees", findByUuid.getFees().toPlainString()).put("blockHeight", height + "").build().toJSONString();
			
		} catch (Exception e) {
			return new JSONBuilder().put("code", "0").put("message","ERROR").build().toJSONString();
		}
	}
	
	
	
}
