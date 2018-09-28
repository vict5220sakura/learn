/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月6日  下午1:49:00
 * @version   V 1.0
 */
package com.bithaw.btc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月6日 下午1:49:00
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
@Slf4j
public class BtcSystemServiceImplTest {
	@Autowired
	BtcSystemService btcSystemService;
	
	@Test
	public void checkNode(){
		btcSystemService.checkNode();
	}
	
	/**
	 * @author WangWei
	 * @Description 构建交易任务
	 * @method buildTradeTaskTest 
	 * @return void
	 * @throws InterruptedException 
	 * @date 2018年9月7日 上午10:12:05
	 */
	@Test
	public void buildTradeTaskTest() throws InterruptedException{
		btcSystemService.buildTradeTask();
		Thread.sleep(3000000L);
	}
	
	@Test
	public void signTaskTest(){
		btcSystemService.signTask();
		try {
			Thread.sleep(300000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void sendRawtransactionTaskTest(){
		btcSystemService.sendRawtransactionTask();
		try {
			Thread.sleep(300000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 确认交易任务
	 * @method ensureTransactionTaskTest 
	 * @return void
	 * @date 2018年9月7日 上午9:56:20
	 */
	@Test
	public void ensureTransactionTaskTest(){
		btcSystemService.ensureTransactionTask();
		try {
			Thread.sleep(300000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
