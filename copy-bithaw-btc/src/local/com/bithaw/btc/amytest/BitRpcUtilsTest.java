package com.bithaw.btc.amytest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;
import com.bithaw.btc.utils.BitRpcUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BitRpcUtilsTest {
	@Autowired
	BitRpcUtils bitRpcUtils;
	
	@Test
	public void getConnectionCountTest(){
		Integer connectionCount = bitRpcUtils.getConnectionCount();
		log.info("connectionCount 测试 : " + connectionCount);
	}
}
