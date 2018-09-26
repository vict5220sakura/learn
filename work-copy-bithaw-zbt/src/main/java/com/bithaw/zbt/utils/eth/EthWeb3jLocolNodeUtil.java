/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  下午3:25:48
 * @version   V 1.0
 */
package com.bithaw.zbt.utils.eth;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

import com.bithaw.zbt.entity.EthTradeNonce;

import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 以太坊本地节点操作工具类
 * @author   WangWei
 * @date     2018年9月11日 下午3:25:48
 * @version  V 1.0
 */
@Slf4j
@Component
public class EthWeb3jLocolNodeUtil {
	@Autowired
	private Web3j web3j;
	
	/**
	 * @author WangWei
	 * @Description 本地节点获取当前区块高度
	 * @method getBlockNumber
	 * @throws IOException 
	 * @return BigInteger
	 * @date 2018年9月11日 下午3:29:10
	 */
	public Optional<BigInteger> getBlockNumber() throws IOException{
		BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
		Optional<BigInteger> blockNumberO = Optional.ofNullable(blockNumber);
		return blockNumberO;
	}
	
	/**
	 * @author WangWei
	 * @Description 获取节点状态
	 * @method checkWeb3Client
	 * @throws IOException 
	 * @return boolean false:错误;true:正确
	 * @date 2018年9月11日 下午3:53:53
	 */
	public boolean checkLocalNode() {
		String version = null;
		try {
			version = web3j.web3ClientVersion().send().getWeb3ClientVersion();
		} catch (IOException e) {
			log.error("以太坊节点链接异常/或网络错误");
			return false;
		}
		return StringUtil.isBlank(version) ? false : true;

	}
	
	/**
	 * @author WangWei
	 * @Description 根据txhash获取交易对象
	 * @method getTransaction
	 * @param transactionHash
	 * @throws IOException 
	 * @return Transaction
	 * @date 2018年9月11日 下午4:06:14
	 */
	public Transaction getTransaction(String transactionHash) throws IOException{
		Transaction transaction = web3j.ethGetTransactionByHash(transactionHash).send().getTransaction().get();
		return transaction;
	}
	
	/**
	 * @author WangWei
	 * @Description 获取交易凭据
	 * @method getTransactionReceipt
	 * @param transactionHash
	 * @throws IOException 
	 * @return TransactionReceipt
	 * @date 2018年9月11日 下午4:15:47
	 */
	public TransactionReceipt getTransactionReceipt(String transactionHash) throws IOException{
		TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().get();
		return transactionReceipt;
	}
	
	/**
	 * @author WangWei
	 * @Description 根据区块高度获取区块实体
	 * @method getBlock
	 * @param blockNumber
	 * @throws IOException 
	 * @return Block
	 * @date 2018年9月11日 下午4:11:43
	 */
	public Block getBlock(BigInteger blockNumber) throws IOException{
		return web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), false).send().getBlock();
	}
	
	/**
	 * 本地节点广播一笔交易
	 * 如果获取到txhash 并且 当前没有txhash 则更新txhash
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public EthSendTransaction sendLocalNode(String rawTransaction) throws Exception{
		log.info("根据order_no 广播一笔交易 装填成功,本地节点广播 开始,rawTransaction : {}",rawTransaction);
		EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(rawTransaction).sendAsync().get();
		log.info("根据order_no 广播一笔交易 装填成功,本地节点广播 结束,rawTransaction : {}",rawTransaction);
		return ethSendTransaction;
	}
	
	/**
	 * @author WangWei
	 * @Description 本地获取一个nonce
	 * @method getNonceLocal
	 * @param address
	 * @return
	 * @throws IOException int
	 * @date 2018年9月12日 下午2:50:54
	 */
	public int getNonceLocal(String address) throws IOException{
		log.info("本地获取nonce:开始,address{}",address);
		BigInteger nonce = BigInteger.ZERO;
		nonce = web3j
				.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send()
				.getTransactionCount();
		log.info("本地获取nonce:成功,address{};nonce{}",address,nonce);
		return nonce.intValue();
	}
}
