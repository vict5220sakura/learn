/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午3:26:11
 * @version   V 1.0
 */
package com.bithaw.zbt.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.zbt.BithawZBTTradeApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月12日 下午3:26:11
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class EthTradeServiceTest {
	@Autowired
	private EthTradeService ethTradeService;
	
	
	@Test
	public void selectFreeze(){
		String zbtTrade = ethTradeService.selectFreeze("0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b");
		log.info(zbtTrade);
		assertTrue(zbtTrade.equals("false"));
	}
	
	@Test
	public void zbtAccountFreeze(){
		String zbtTrade = ethTradeService.zbtAccountFreeze("5233", "0x763b78bc83ef328a252b25b56ba2a7fe49774b72", "0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b");
		log.info(zbtTrade);
		assertTrue(zbtTrade.equals("SUCCESS"));
	}
	
	@Test
	public void zbtAccountUnfreeze(){
		String zbtTrade = ethTradeService.zbtAccountUnfreeze("5234", "0x763b78bc83ef328a252b25b56ba2a7fe49774b72", "0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b");
		log.info(zbtTrade);
		assertTrue(zbtTrade.equals("SUCCESS"));
	}
	
	@Test
	public void zbtTrade(){
		String zbtTrade = ethTradeService.zbtTrade("5236", "0x763b78bc83ef328a252b25b56ba2a7fe49774b72", "0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b", "0.00012");
		log.info(zbtTrade);
	}

	@Test
	public void zbtTradeCover(){
		String zbtTradeCover = ethTradeService.zbtTradeCover("007","5235", "7", "0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b", "0.0053");
		assertTrue(zbtTradeCover.equals("SUCCESS"));
	}
	
}
