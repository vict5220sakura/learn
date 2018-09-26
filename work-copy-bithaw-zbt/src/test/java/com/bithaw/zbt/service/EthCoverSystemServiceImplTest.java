/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月13日  下午1:48:56
 * @version   V 1.0
 */
package com.bithaw.zbt.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.zbt.BithawZBTTradeApplication;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月13日 下午1:48:56
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class EthCoverSystemServiceImplTest {
	@Autowired
	EthCoverSystemService ethCoverSystemService;
	
	@Test
	public void setNonceTask() throws InterruptedException{
		ethCoverSystemService.setNonceTask();
		Thread.sleep(30000L);
	}
	
	@Test
	public void localSignTask() throws InterruptedException{
		ethCoverSystemService.localSignTask();
		Thread.sleep(30000L);
	}
	
	@Test
	public void sendTask() throws InterruptedException{
		ethCoverSystemService.sendTask();
		Thread.sleep(30000L);
	}
	
	@Test
	public void etherscanRepairTxhashTask() throws InterruptedException{
		ethCoverSystemService.etherscanRepairTxhashTask();
		Thread.sleep(30000L);
	}
	
	@Test
	public void ensureTxByEtherscanTask() throws InterruptedException{
		ethCoverSystemService.ensureTxByEtherscanTask();
		Thread.sleep(30000L);
	}
	
	@Test
	public void finalStateTask() throws InterruptedException{
		ethCoverSystemService.finalStateTask();
		Thread.sleep(30000L);
	}
}
