/**
 * @Description EthSendServiceImpl junit测试文件
 * @author  WangWei
 * @Date    2018年8月27日  上午10:21:13
 * @version   V 1.0
 */
package com.bithaw.zbt.service;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.entity.EthTradeNonce;
import com.bithaw.zbt.mapper.EthTradeNonceMapper;


/**
 * @Description EthSendServiceImpl junit测试类
 * @author   WangWei
 * @date     2018年8月27日 上午10:21:13
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
//@Transactional
public class EthSystemServiceImplTest {
	@Autowired
	private EthSystemService ethSystemService;
	
	@Autowired
	private EthTradeNonceMapper ethTradeNonceMapper;
	
	//@Before
	public void brfore(){
		EthTradeNonce build = new EthTradeNonce.Builder()//
			.setCreateTime(new Date())//
			.setData("TEST")//
			.setFromAddress("testFromAddress")//
			.setGasLimit(0)//
			.setGasPrice(new BigDecimal("0"))//
			.setNonce(0)//
			.setOrderNo("testOrderNo")//
			.setRawTransaction("testRawTransaction")//
			.setState(0)//
			.setToAddress("testToAddress")//
			.setTxhash("testTxHash")//
			.setValue(new BigDecimal("0"))//
			.build();
		ethTradeNonceMapper.save(build);
	}
	
	@Test
	public void getStateTest(){
		int state = ethSystemService.getState("testOrderNo");
		assertTrue(state == 0);
		int stateNull = ethSystemService.getState("testOrderNoNull");
		assertTrue(stateNull == -1);
	}
	
	//1
	@Test
	public void setNonceTask() throws InterruptedException{
		ethSystemService.setNonceTask();
		Thread.sleep(30000L);
	}
	
	//2
	@Test
	public void localSignTask() throws InterruptedException{
		ethSystemService.localSignTask();
		Thread.sleep(30000L);
	}
	
	//3
	@Test
	public void sendTask() throws InterruptedException{
		ethSystemService.sendTask();
		Thread.sleep(30000L);
	}
	
	//4
	@Test
	public void ensureTxByEtherscanTask() throws InterruptedException{
		ethSystemService.ensureTxByEtherscanTask();
		Thread.sleep(30000L);
	}
	
	//Repair
	@Test
	public void etherscanRepairTxhashTask() throws InterruptedException{
		ethSystemService.etherscanRepairTxhashTask();
		Thread.sleep(30000);
	}
}
