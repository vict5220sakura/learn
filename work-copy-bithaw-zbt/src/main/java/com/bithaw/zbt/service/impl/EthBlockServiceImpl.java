package com.bithaw.zbt.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bithaw.zbt.common.Common;
import com.bithaw.zbt.entity.BlockChainRecord;
import com.bithaw.zbt.entity.BlockChainTradeRecord;
import com.bithaw.zbt.entity.BlockEntity;
import com.bithaw.zbt.entity.EtherscanTxByTxlist;
import com.bithaw.zbt.feign.SysConfigClient;
import com.bithaw.zbt.mapper.BlockChainRecordMapper;
import com.bithaw.zbt.mapper.BlockChainTradeRecordMapper;
import com.bithaw.zbt.service.EthBlockService;
import com.bithaw.zbt.solidity.bhaw.v1.BhawV1;
import com.bithaw.zbt.utils.RedisComponent;
import com.bithaw.zbt.utils.eth.EthWeb3jLocolNodeUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 以太坊区块操作服务
 * @author   WangWei
 * @date     2018年8月24日 上午11:53:28
 * @version  V 1.0
 */
@Slf4j
@Service
public class EthBlockServiceImpl implements EthBlockService {

	@Autowired
	private BlockChainRecordMapper blockChainRecordMapper;
	@Autowired
	private BlockChainTradeRecordMapper blockChainTradeRecordMapper;
	@Autowired
	private SysConfigClient sysConfigClient;
	@Autowired
    private RedisComponent redisComponent;
	
	@Value("${spring.profiles.active}")
	private String active;
	
	@Autowired
	private EthWeb3jLocolNodeUtil ethWeb3jLocolNodeUtil;
	
	private String solidityAddress;
	
	//扫描区块多线程配置前缀
	String scanBlockHeightPrefix = "eth_scan_block_height_";
	
	/**
	 * ethScanBlockHeightThreadCount : 本地节点扫描区块线程数
	 */
	private int ethScanBlockHeightThreadCount;
	
	/**
	 * prefix : 扫描区块多线程配置前缀
	 */
	String prefix = "eth_scan_block_height_";
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	/** 
	 * <p>Title: init</p>
	 * <p>Description: 初始化获取配置的solidityAddress地址</p>
	 * @see com.bithaw.zbt.service.EthBlockService#init()  
	 */
	@Override
	public void init() {
		log.info("初始化 开始");
		solidityAddress = BhawV1.solidityAddress;
		
		for(int i = 0 ; i < 50 ; i++){
			if( ! Boolean.parseBoolean(sysConfigClient.isKeyExist(prefix + i)) ){
				ethScanBlockHeightThreadCount = i;
				break;
			}
		}
		log.info("初始化 结束 solidityAddress: {}",solidityAddress);
	}

	/** 
	 * <p>Title: scanBlockLocalTask</p>
	 * <p>Description: </p>
	 * @see com.bithaw.zbt.service.EthBlockService#scanBlockLocalTask()  
	 */
	@Override
	public void scanBlockLocalTask() {
		log.info("本地节点同步任务,总线程数: {}",ethScanBlockHeightThreadCount);
		EthBlockService thisbean = applicationContext.getBean(EthBlockService.class);
		for(int threadIndex = 0; threadIndex < ethScanBlockHeightThreadCount ; threadIndex++){
			thisbean.scanBlockLocalAsync(ethScanBlockHeightThreadCount,threadIndex);//多线程执行
		}
		return;
	}
	
	/** 
	 * <p>Title: scanBlockLocalAynsc</p>
	 * <p>Description: 扫描区块任务,异步执行</p>
	 * @param ethScanBlockHeightThreadCount
	 * @param threadIndex
	 * @see com.bithaw.zbt.service.EthBlockService#scanBlockLocalAynsc(int, int)  
	 */
	@Async
	@Override
	public void scanBlockLocalAsync(int ethScanBlockHeightThreadCount, int threadIndex) {
		if(redisComponent.acquire(active + "_SYNCHRONIZATION_KEY_" + threadIndex, 10L * 60L)){//尝试获取其中一个线程锁
			try {
				log.info("加锁 成功,开始同步区块,当前锁: {},总线程数: {};当前线程: {}",active + "_SYNCHRONIZATION_KEY_" + threadIndex,ethScanBlockHeightThreadCount,threadIndex);
				
				scanBlockLocalStart(ethScanBlockHeightThreadCount, threadIndex);
			} catch (Exception e) {
				log.error("eth本地节点扫描同步出错",e);
			} finally{
				redisComponent.release(active + "_SYNCHRONIZATION_KEY_"  + threadIndex);
				log.info("redis释放{}锁",active + "_SYNCHRONIZATION_KEY_" + threadIndex);
			}
		}else{
			log.info("被锁定,忽略执行, 当前线程锁: {}",active + "_SYNCHRONIZATION_KEY_" + threadIndex);
		}
	}
	/**
	 * @author WangWei
	 * @Description 异步执行同步区块任务
	 * @method scanBlockLocalStart
	 * @param ethScanBlockHeightThreadCount 总线程数
	 * @param threadIndex 当前线程角标0开始
	 * @throws RuntimeException
	 * @throws IOException 
	 * @return void
	 * @date 2018年9月11日 下午6:06:50
	 */
	private void scanBlockLocalStart(int ethScanBlockHeightThreadCount, int threadIndex) throws RuntimeException, IOException {
		BigInteger blockNodehighest = ethWeb3jLocolNodeUtil.getBlockNumber().orElseThrow(() -> new RuntimeException("获取高度异常")).subtract(new BigInteger("7"));//节点最高可以同步的区块
		String scanHeight = sysConfigClient.getSysConfigValue(scanBlockHeightPrefix + threadIndex);
		BigInteger scanBlockHeightStart = new BigInteger(scanHeight).add(new BigInteger(ethScanBlockHeightThreadCount + ""));//当前已扫描到的区块 + 线程数 = 开始扫描的区块号
		log.info("当前节点最高可同步区块(被6区块确认)区块: {} 线程{} 已经扫描到{} 若要扫描,第一个区块是 {} ",blockNodehighest,threadIndex,scanHeight,scanBlockHeightStart);
		if(scanBlockHeightStart.add(new BigInteger("10").multiply(new BigInteger(ethScanBlockHeightThreadCount + ""))).compareTo(blockNodehighest) > 0){
			log.info("线程{}不足10个区块差距,同步到最新区块",threadIndex);
			if(scanBlockHeightStart.compareTo(blockNodehighest) < 0){
				//同步到最新区块
				log.info("线程{}开始同步到最新区块,当前准备描区块高度{},区块链最高区块高度{},当前线程数{}",threadIndex,scanBlockHeightStart,blockNodehighest,ethScanBlockHeightThreadCount);
				int blockNodehighestAlign;
				for(blockNodehighestAlign = blockNodehighest.intValue() ; blockNodehighestAlign % ethScanBlockHeightThreadCount != threadIndex ; blockNodehighestAlign--){
					
				}
				log.info("线程{}对齐最高区块{},对齐后{}",threadIndex,blockNodehighest,blockNodehighestAlign);
				log.info("线程{}开始同步区块,同步起始{},结束{}",threadIndex,scanBlockHeightStart,blockNodehighestAlign);
				scanBlockLocal(scanBlockHeightStart, new BigInteger(blockNodehighestAlign + ""),ethScanBlockHeightThreadCount,threadIndex);
				log.info("线程{}同步区块结束,同步起始{},结束{}",threadIndex,scanBlockHeightStart,blockNodehighestAlign);
				sysConfigClient.setSysConfigValue(scanBlockHeightPrefix + threadIndex, String.valueOf(blockNodehighestAlign - ethScanBlockHeightThreadCount));
				log.info("线程{}同步区块 更新配置状态,同步起始{},同步结束{}",threadIndex,scanBlockHeightStart,blockNodehighestAlign);
			}else{
				//已经是最新区块无需扫描
				log.info("线程{}已经是最新区块无需扫描",threadIndex);
			}
		}else{
			//同步10个区块
			BigInteger scanBlockHeightEnd = scanBlockHeightStart.add(new BigInteger("10").multiply(new BigInteger(ethScanBlockHeightThreadCount + "")));
			log.info("线程{}同步10个区块,开始区块{},结束区块",threadIndex,scanBlockHeightStart,scanBlockHeightEnd);
			scanBlockLocal(scanBlockHeightStart, scanBlockHeightEnd,ethScanBlockHeightThreadCount,threadIndex);
			log.info("线程{}同步10个区块 结束,开始区块{}",threadIndex,scanBlockHeightStart);
			sysConfigClient.setSysConfigValue(scanBlockHeightPrefix + threadIndex, String.valueOf(scanBlockHeightEnd.intValue() - ethScanBlockHeightThreadCount));
			log.info("线程{}同步区块 更新配置状态,同步起始{},同步结束{}",threadIndex,scanBlockHeightStart,scanBlockHeightEnd);
		}
		log.info("eth本地节点扫描同步完成");
	}

	/**
	 * @author WangWei
	 * @Description 扫描我们需要的交易,存储到数据库,更新parameter参数表中最高区块的内容
	 * @method scanBlockLocal
	 * @param startBlock 开始区块
	 * @param endBlock 结束区块(不包含)
	 * @param ethScanBlockHeightThreadCount
	 * @param threadIndex
	 * @throws IOException 
	 * @return void
	 * @date 2018年9月11日 下午6:08:11
	 */
	private void scanBlockLocal(BigInteger startBlock,BigInteger endBlock, int ethScanBlockHeightThreadCount, int threadIndex) throws IOException {
		log.info("线程{}开始扫描区块,开始位置{},结束位置{},线程总数{}",threadIndex,startBlock,endBlock,ethScanBlockHeightThreadCount);
		if(startBlock.compareTo(endBlock) >= 0){
			return;
		}
		BigInteger blockNumberNodeHighest = ethWeb3jLocolNodeUtil.getBlockNumber().orElseThrow(() -> new RuntimeException("获取高度异常"));
		log.info("当前区块实际高度{}",blockNumberNodeHighest);
		if(endBlock.compareTo(blockNumberNodeHighest.subtract(new BigInteger("6"))) > 0){
			return;
		}
		for(int i = startBlock.intValue(); i < endBlock.intValue() ; i += ethScanBlockHeightThreadCount){
			BlockEntity block = getBlockInfoByNumber(new BigInteger(i + ""));
			List<BlockChainRecord> BlockChainRecordList = convert(block);
			if(BlockChainRecordList==null){
				//区块可能没有交易
			}else{
				for(BlockChainRecord blockChainRecord : BlockChainRecordList){
					if(blockChainRecord.getBlockChainTradeRecord() != null){
						try {
							blockChainRecordMapper.save(blockChainRecord);
						} catch (Throwable e) {
							log.info("插入出错,可能是已经存在,错误日志{}",e);
						} 
						try {
							blockChainTradeRecordMapper.save(blockChainRecord.getBlockChainTradeRecord());
						} catch (Throwable e) {
							log.info("插入出错,可能是已经存在,错误日志{}",e);
						}
					}
				}
			}
		}
		log.info("线程{}扫描区块结束,开始位置{},结束位置{},线程总数{}",threadIndex,startBlock,endBlock,ethScanBlockHeightThreadCount);
	}

	/** 
	 * <p>Title: getBlockInfoByNumber</p>
	 * <p>Description: 根据区块高度号获取区块实体对象</p>
	 * @param blockNumber
	 * @return
	 * @throws IOException
	 * @see com.bithaw.zbt.service.EthBlockService#getBlockInfoByNumber(java.math.BigInteger)  
	 */
	private BlockEntity getBlockInfoByNumber(BigInteger blockNumber) throws IOException {
		BigInteger blockNumberNodeHighest = ethWeb3jLocolNodeUtil.getBlockNumber().orElseThrow( () -> new RuntimeException("获取高度异常"));
		log.info("根据区块号查询区块实体,查询高度{},当前区块高度{},区块相差{}",blockNumber,blockNumberNodeHighest,blockNumberNodeHighest.subtract(blockNumber));
		if(blockNumber.compareTo(blockNumberNodeHighest) > 0 ){
			log.info("查询高度超过区块高度,查询失败");
			return null;
		}
		
		Block block = ethWeb3jLocolNodeUtil.getBlock(blockNumber);
		if(block != null){
			BlockEntity blockEntity = new BlockEntity();
			blockEntity.setBlockNumber(blockNumber);
			blockEntity.setBlockHash(block.getHash());
			blockEntity.setDate(new Date(Long.parseLong(block.getTimestamp().toString()+"000")));
			List<TransactionResult> transactions = block.getTransactions();
			List<String> transactionHashList = new ArrayList<>();
			for(TransactionResult transactionResult : transactions){
				transactionHashList.add((String) transactionResult.get());
			}
			blockEntity.setTransactionHashList(transactionHashList);
			log.info("根据区块号查询区块实体成功,查询高度{},当前区块高度{},区块相差{}",blockNumber,blockNumberNodeHighest,blockNumberNodeHighest.subtract(blockNumber));
			return blockEntity;
		}
		log.error("根据区块号查询区块实体失败,查询高度{},当前区块高度{},区块相差{}",blockNumber,blockNumberNodeHighest,blockNumberNodeHighest.subtract(blockNumber));
		return null;
	}

	/** 
	 * <p>Title: convert</p>
	 * <p>Description: </p>
	 * @param blockEntity
	 * @return
	 * @throws IOException
	 * @see com.bithaw.zbt.service.EthBlockService#convert(com.bithaw.zbt.entity.BlockEntity)  
	 */
	private List<BlockChainRecord> convert(BlockEntity blockEntity) throws IOException{
		log.info("区块实体{}转交易实体集合");
		List<BlockChainRecord> returnList = new ArrayList<BlockChainRecord>();
		if(blockEntity.getTransactionHashList().size()==0){
			return null;
		}
		for(String transactionHash : blockEntity.getTransactionHashList()){
			BlockChainRecord blockChainRecord = new BlockChainRecord();
			blockChainRecord.setBlockHeight(blockEntity.getBlockNumber());
			blockChainRecord.setAddTime(new BigInteger(blockEntity.getDate().getTime() + ""));
			Transaction transaction = ethWeb3jLocolNodeUtil.getTransaction(transactionHash);
			TransactionReceipt transactionReceipt = ethWeb3jLocolNodeUtil.getTransactionReceipt(transactionHash);
			blockChainRecord.setTxHash(transactionReceipt.getTransactionHash());
			blockChainRecord.setFromAddress(transactionReceipt.getFrom());
			blockChainRecord.setToAddress(transactionReceipt.getTo());
			blockChainRecord.setContent(transaction.getInput());
			blockChainRecord.setAmount(new BigDecimal(transaction.getValue().toString()).divide(Common.B1x10_18));
			BigDecimal fee = new BigDecimal(transaction.getGasPrice().multiply(transactionReceipt.getGasUsed()).toString()).divide(Common.B1x10_18);
			blockChainRecord.setActualServiceFee(fee);
			if(transactionReceipt.isStatusOK()){
				blockChainRecord.setStatus(1);
			}else{
				blockChainRecord.setStatus(2);
			}
			
			//当是我们需要的合约时
			if(transactionReceipt.getTo()!=null &&//
					transaction.getInput()!=null && //
					transactionReceipt.getTo().toUpperCase().equals(solidityAddress.toUpperCase()) && //
					transaction.getInput().toUpperCase().startsWith("0xa9059cbb".toUpperCase())){
				//表明调用了合约发送币的方法
				
				String contentToAddress = transaction.getInput().substring(10 + (64 - 40), 74);
				blockChainRecord.setContractToAddress("0x" + contentToAddress);
				BigDecimal contractAmount = new BigDecimal(Long.parseLong(transaction.getInput().substring(74, 138), 16)).divide(new BigDecimal("1000000"));
				blockChainRecord.setContractAmount(contractAmount);
				
				//构造合约交易信息实体 存入数据库
				BlockChainTradeRecord blockChainTradeRecord = new BlockChainTradeRecord();
				blockChainTradeRecord.setFromAddress(blockChainRecord.getFromAddress());
				blockChainTradeRecord.setToAddress(blockChainRecord.getContractToAddress());
				blockChainTradeRecord.setAmount(blockChainRecord.getContractAmount());
				blockChainTradeRecord.setActualServiceFee(blockChainRecord.getActualServiceFee());
				blockChainTradeRecord.setTxHash(blockChainRecord.getTxHash());
				blockChainTradeRecord.setBlockHeight(blockChainRecord.getBlockHeight());
				blockChainTradeRecord.setSolidityAddress(blockChainRecord.getToAddress());
				if(transactionReceipt.isStatusOK()){
					blockChainTradeRecord.setNotifyStatus(0);
					blockChainTradeRecord.setStatus(1);
				}else{
					blockChainTradeRecord.setNotifyStatus(2);
					blockChainTradeRecord.setStatus(2);
				}
				blockChainTradeRecord.setAddTime(blockChainRecord.getAddTime());
				
				blockChainRecord.setBlockChainTradeRecord(blockChainTradeRecord);
			}
			
			returnList.add(blockChainRecord);
		}
		return returnList;
	}

	/** 
	 * <p>Title: scanBlockEtherscanTask</p>
	 * <p>Description: Etherscan同步数据接口</p>
	 * @see com.bithaw.zbt.service.EthBlockService#scanBlockEtherscanTask()  
	 */
	@Override
	public void scanBlockEtherscanTask() {
		log.info("Etherscan同步数据接口");
		EthBlockService thisbean = applicationContext.getBean(EthBlockService.class);
		thisbean.scanBlockEtherscanAsync();
	}
	
	/** 
	 * <p>Title: scanBlockEtherscanAsync</p>
	 * <p>Description: Etherscan同步数据接口异步调用</p>
	 * @see com.bithaw.zbt.service.EthBlockService#scanBlockEtherscanAsync()  
	 */
	@Async
	@Override
	public void scanBlockEtherscanAsync() {
		log.info("Etherscan查询solidity合约查询写入数据库任务 开始");
		if(redisComponent.acquire(active + "_SYNCHRONIZATION_KEY_ETHERSCAN", 10L * 60L)){
			try {
				log.info("加锁 成功,开始同步区块,当前锁: {}",active + "_SYNCHRONIZATION_KEY_ETHERSCAN");
				scanBlockEtherscanStart();//多线程执行
			} catch (Exception e) {
				log.error("Etherscan同步数据接口异步调用出错",e);
			} finally{
				redisComponent.release(active + "_SYNCHRONIZATION_KEY_ETHERSCAN");
				log.info("redis释放{}锁",active + "_SYNCHRONIZATION_KEY_ETHERSCAN");
			}
		}else{
			log.info("被锁定,忽略执行, 当前线程锁: {}",active + "_SYNCHRONIZATION_KEY_ETHERSCAN");
		}
		log.info("Etherscan查询solidity合约查询写入数据库任务 结束");
	}

	/**
	 * @author WangWei
	 * @Description 
	 * @method scanBlockEtherscanStart 
	 * @return void
	 * @date 2018年9月11日 下午6:18:56
	 */
	private void scanBlockEtherscanStart() {
		log.info("scanBlockEtherscanStart开始");
		BigInteger maxBlockNumberByToAddress = blockChainRecordMapper.getMaxBlockNumberByToAddress(solidityAddress);
		maxBlockNumberByToAddress = maxBlockNumberByToAddress == null ? new BigInteger("0") : maxBlockNumberByToAddress;
		List<EtherscanTxByTxlist> etherscanGetAllTXByAddress = etherscanGetAllTXByAddress(solidityAddress,maxBlockNumberByToAddress);//扫描合约地址全部交易
		etherscanGetAllTXByAddress = filter(etherscanGetAllTXByAddress, maxBlockNumberByToAddress);
		List<BlockChainRecord> convertToBlockChainRecord = convertToBlockChainRecord(etherscanGetAllTXByAddress);
		List<BlockChainTradeRecord> convertToBlockChainTradeRecord = convertToBlockChainTradeRecord(etherscanGetAllTXByAddress);
		for(BlockChainRecord blockChainRecord : convertToBlockChainRecord){
			try {
				blockChainRecordMapper.save(blockChainRecord);
			} catch (Throwable e) {
				log.info("插入错误,可能已经存在,错误日志",e);
			}
		}
		for(BlockChainTradeRecord blockChainTradeRecord : convertToBlockChainTradeRecord){
			try {
				blockChainTradeRecordMapper.save(blockChainTradeRecord);
			} catch (Throwable e) {
				log.info("插入错误,可能已经存在,错误日志",e);
			}
		}
	}
	
	
	/**
	 * 第三方接口获取一个地址的全部交易 封装到对象
	 */ 
	private List<EtherscanTxByTxlist> etherscanGetAllTXByAddress(String address,BigInteger maxBlockNumberByToAddress){
		log.info("Etherscan查找账户的全部交易 开始,address{}",address);
		String etherscanApikey = sysConfigClient.getSysConfigValue("etherscan_apikey");
		String etherscanUrl = sysConfigClient.getSysConfigValue("etherscan_url");
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "account");
		map.add("action", "txlist");
		map.add("address", address);
		map.add("startblock", maxBlockNumberByToAddress.toString());
		map.add("endblock", "99999999");
		map.add("sort", "aes");
		map.add("apikey", etherscanApikey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity(etherscanUrl, request, JSONObject.class);
		log.info("Etherscan查找账户的全部交易 网络访问开始,address{}",address);
		JSONArray jsonArray = postForEntity.getBody().getJSONArray("result");
		if(jsonArray==null){
			log.error("查找账户的全部交易 开始 失败 网络访问失败,address{}",address);
			return null;
		}
		ArrayList<EtherscanTxByTxlist> returnList = new ArrayList<EtherscanTxByTxlist>();
		for(int i = 0 ; i < jsonArray.size() ; i++){
			EtherscanTxByTxlist etherscanTxByTxlist = new EtherscanTxByTxlist.Builder()
				.setBlockHash(jsonArray.getJSONObject(i).getString("blockHash"))
				.setBlockNumber(jsonArray.getJSONObject(i).getString("blockNumber"))
				.setConfirmations(jsonArray.getJSONObject(i).getString("confirmations"))
				.setContractAddress(jsonArray.getJSONObject(i).getString("contractAddress"))
				.setCumulativeGasUsed(jsonArray.getJSONObject(i).getString("cumulativeGasUsed"))
				.setFrom(jsonArray.getJSONObject(i).getString("from"))
				.setGas(jsonArray.getJSONObject(i).getString("gas"))
				.setGasPrice(jsonArray.getJSONObject(i).getString("gasPrice"))
				.setGasUsed(jsonArray.getJSONObject(i).getString("gasUsed"))
				.setHash(jsonArray.getJSONObject(i).getString("hash"))
				.setInput(jsonArray.getJSONObject(i).getString("input"))
				.setIsError(jsonArray.getJSONObject(i).getString("isError"))
				.setNonce(jsonArray.getJSONObject(i).getString("nonce"))
				.setTimeStamp(jsonArray.getJSONObject(i).getString("timeStamp"))
				.setTo(jsonArray.getJSONObject(i).getString("to"))
				.setTransactionIndex(jsonArray.getJSONObject(i).getString("transactionIndex"))
				.setTxreceipt_status(jsonArray.getJSONObject(i).getString("txreceipt_status"))
				.setValue(jsonArray.getJSONObject(i).getString("value")).build();
			returnList.add(etherscanTxByTxlist);
		}
		log.info("Etherscan查找账户的全部交易 结束,address{}",address);
		return returnList;
	}

	/**
	 * 过滤掉小于一定高度的数据
	 * <=blockHeighest的交易过滤掉
	 */
	private List<EtherscanTxByTxlist> filter(List<EtherscanTxByTxlist> etherscanTxByTxlist,BigInteger blockHeighest){
		log.info("过滤一些已有的交易开始");
		ArrayList<EtherscanTxByTxlist> returnList = new ArrayList<EtherscanTxByTxlist>();
		for(EtherscanTxByTxlist etherscanTx :etherscanTxByTxlist){
			BigInteger etherscanTxBlockHeight = new BigInteger(etherscanTx.getBlockNumber());
			int compareTo = etherscanTxBlockHeight.compareTo(blockHeighest);
			if( compareTo > 0 ){//获取到的区块高度大于数据库区块高度
				returnList.add(etherscanTx);
			}
		}
		log.info("过滤一些已有的交易结束");
		return returnList;
	}
	
	//转换第三方接口交易到数据库实体类
	private List<BlockChainRecord> convertToBlockChainRecord(List<EtherscanTxByTxlist> etherscanTxByTxlist){
		log.info("转换etherscan数据到实体开始");
		ArrayList<BlockChainRecord> arrayList = new ArrayList<BlockChainRecord>();
		for(EtherscanTxByTxlist etherscanTx : etherscanTxByTxlist){
			BlockChainRecord blockChainRecord = new BlockChainRecord.Builder()
				.setBlockHeight(new BigInteger(etherscanTx.getBlockNumber()))
				.setAddTime(new BigInteger(etherscanTx.getTimeStamp()+"000"))
				.setTxHash(etherscanTx.getHash())
				.setFromAddress(etherscanTx.getFrom())
				.setToAddress(etherscanTx.getTo())
				.setContent(etherscanTx.getInput())
				.setAmount( new BigDecimal(etherscanTx.getValue()).divide(Common.B1x10_18, 18, BigDecimal.ROUND_HALF_UP) )
				.setCoinType("ETH")
				.setActualServiceFee( 
						new BigDecimal(etherscanTx.getGasPrice()) 
						.multiply(new BigDecimal(etherscanTx.getGasUsed())) 
						.divide(Common.B1x10_18, 18, BigDecimal.ROUND_HALF_UP)  )
				.setStatus(etherscanTx.getIsError().equals("0") ? 1 : 2) 
				.build();
			if(blockChainRecord.getContent() != null//
					&& blockChainRecord.getToAddress().toUpperCase() .equals (solidityAddress.toUpperCase() )
					&& blockChainRecord.getContent().toUpperCase() .startsWith ("0xa9059cbb".toUpperCase()) 
					&& blockChainRecord.getContent().length() == (64+64+10)){//判断是调用合约转账方法
				String contentToAddress = "0x"+blockChainRecord.getContent().substring(10 + (64 - 40), 74);
				BigDecimal contractAmount = new BigDecimal( Long.parseLong(  blockChainRecord.getContent() .substring(74, 138)  ,  16) ) 
						.divide(new BigDecimal("1000000") );
				blockChainRecord.setContractToAddress(contentToAddress);
				blockChainRecord.setContractAmount(contractAmount);
			}
			arrayList.add(blockChainRecord);
		}
		log.info("转换etherscan数据到实体结束");
		return arrayList;
	}
	
	//转换第三方接口交易到合约交易实体
	private List<BlockChainTradeRecord> convertToBlockChainTradeRecord(List<EtherscanTxByTxlist> etherscanTxByTxlist){
		log.info("转换etherscan数据到实体开始");
		ArrayList<BlockChainTradeRecord> arrayList = new ArrayList<BlockChainTradeRecord>();
		for(EtherscanTxByTxlist etherscanTx : etherscanTxByTxlist){
			if(etherscanTx.getInput() != null
					&& etherscanTx.getTo().toUpperCase() .equals (solidityAddress.toUpperCase() )
					&& etherscanTx.getInput().toUpperCase() .startsWith ("0xa9059cbb".toUpperCase()) 
					&& etherscanTx.getInput().length() == (64+64+10)){//判断是调用合约转账方法
			}else{
				//如果不是则跳过
				continue;
			}
			String contentToAddress = "0x"+etherscanTx.getInput().substring(10 + (64 - 40), 74);
			BigDecimal contractAmount = new BigDecimal( Long.parseLong(  etherscanTx.getInput() .substring(74, 138)  ,  16) ) 
					.divide(new BigDecimal("1000000") );
			BlockChainTradeRecord blockChainTradeRecord = new BlockChainTradeRecord.Builder()
				.setFromAddress(etherscanTx.getFrom())
				.setToAddress(contentToAddress)
				.setAmount(contractAmount)
				.setCoinType("ZBT")
				.setActualServiceFee(
					new BigDecimal( etherscanTx.getGasPrice() ) 
					.multiply( new BigDecimal(etherscanTx.getGasUsed()) ) 
					.divide(Common.B1x10_18, 18, BigDecimal.ROUND_HALF_UP)  ) 
				.setTxHash(etherscanTx.getHash())
				.setBlockHeight(new BigInteger(etherscanTx.getBlockNumber()))
				.setSolidityAddress(etherscanTx.getTo())
				.setNotifyStatus( etherscanTx.getIsError().equals("0") ? 0 : 2 )
				.setStatus( etherscanTx.getIsError().equals("0") ? 1 : 2 )
				.setAddTime( new BigInteger(etherscanTx.getTimeStamp()+"000") )
				.build();
			arrayList.add(blockChainTradeRecord);
		}
		log.info("转换etherscan数据到实体结束");
		return arrayList;
	}
	
}
