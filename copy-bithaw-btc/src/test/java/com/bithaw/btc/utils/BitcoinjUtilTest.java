/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午5:21:27
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午5:21:27
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BitcoinjUtilTest {
	@Autowired
	BitcoinjUtil bitcoinjUtil;
	
	@Test
	public void checkAddressTest(){
		boolean checkAddress = bitcoinjUtil.checkAddress("1278PmjsztTdkfq943KTSd7nfVXy5ptQ89");
		assertTrue(checkAddress);
	}
}
