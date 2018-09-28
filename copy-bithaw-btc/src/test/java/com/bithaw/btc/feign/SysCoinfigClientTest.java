/**
 * @Description SysCoinfigClient junit测试文件
 * @author  WangWei
 * @Date    2018年8月28日  下午1:37:30
 * @version   V 1.0
 */
package com.bithaw.btc.feign;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;

/**
 * @Description SysCoinfigClient junit测试类
 * @author   WangWei
 * @date     2018年8月28日 下午1:37:30
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class SysCoinfigClientTest {
	@Autowired
	SysCoinfigClient sysCoinfigClient;
	
	@Test
	public void getSysConfigValueTest(){
		String value = System.currentTimeMillis() + "";
		sysCoinfigClient.setSysConfigValue("junit_test_sysconfig_set", value);
		String sysConfigValue = sysCoinfigClient.getSysConfigValue("junit_test_sysconfig_set");
		assertTrue(value.equals(sysConfigValue));
		String sysConfigValue2 = sysCoinfigClient.getSysConfigValue("junit_test_sysconfig_set_null");
		assertTrue(StringUtil.isBlank(sysConfigValue2));
	}
}
