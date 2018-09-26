/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  下午3:36:22
 * @version   V 1.0
 */
package com.bithaw.zbt.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.utils.eth.EthWeb3jLocolNodeUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月11日 下午3:36:22
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class EthWeb3jLocolNodeUtilTest {
	@Autowired
	private EthWeb3jLocolNodeUtil ethWeb3jLocolNodeUtil;
	
	@Test
	public void getBlockNumberTest() throws IOException {
		Optional<BigInteger> blockNumber = ethWeb3jLocolNodeUtil.getBlockNumber();
		assertTrue(blockNumber.isPresent());
		log.info(blockNumber.toString());
	}
	
	@Test
	public void checkWeb3ClientTest() {
		boolean flag = ethWeb3jLocolNodeUtil.checkLocalNode();
		assertTrue(flag);
	}
	
	@Test
	public void getTransactionTest() throws IOException {
		Transaction transaction = ethWeb3jLocolNodeUtil.getTransaction("0xb7480a8e239cc98d7730bd5c02fd21fe5cd4a78036755028f413cf50d340cea3");
		assertTrue(transaction != null);
		log.info(transaction.getFrom());
	}
	@Test
	public void getTransactionReceiptTest() throws IOException {
		TransactionReceipt transactionReceipt = ethWeb3jLocolNodeUtil.getTransactionReceipt("0xb7480a8e239cc98d7730bd5c02fd21fe5cd4a78036755028f413cf50d340cea3");
		assertTrue(transactionReceipt != null);
		log.info(transactionReceipt.getBlockHash());
	}
	
	@Test
	public void getBlockTest() throws IOException {
		Block block = ethWeb3jLocolNodeUtil.getBlock(new BigInteger("123"));
		assertTrue(block != null);
		log.info(block.getHash());
	}
}
