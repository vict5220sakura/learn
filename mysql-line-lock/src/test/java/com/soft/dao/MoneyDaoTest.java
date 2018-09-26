/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月19日  下午1:47:46
 * @version   V 1.0
 */
package com.soft.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.start.LineLockApplication;
import com.start.service.MoneyService;


/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月19日 下午1:47:46
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LineLockApplication.class)
public class MoneyDaoTest {
	
	@Autowired
	private MoneyService moneyService;
	
	@Test
	public void update1(){
		moneyService.pay(1);
	}
	
	@Test
	public void update2(){
		moneyService.pay(1);
	}
}
