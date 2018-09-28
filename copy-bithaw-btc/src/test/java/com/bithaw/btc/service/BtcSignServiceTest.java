/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月7日  下午5:24:52
 * @version   V 1.0
 */
package com.bithaw.btc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.BithawBtcApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月7日 下午5:24:52
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
@Slf4j
public class BtcSignServiceTest {
	@Autowired
	BtcSignService btcSignService;
	
	@Test
	public void getAwaitDataTest(){
		JSONObject json = btcSignService.getAwaitData();
		log.info(json.toJSONString());
	}
}
