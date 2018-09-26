package com.bithaw.zbt.feign;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bithaw.zbt.BithawZBTTradeApplication;

/**
 * @Description SysConfigClient junit测试类
 * @author   WangWei
 * @date     2018年8月24日 下午1:47:06
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
@Transactional
public class SysConfigClientTest {
	@Autowired
	private SysConfigClient sysConfigClient;
	
	/**
	 * @author WangWei
	 * @Description 测试获取配置参数,需要先启动eureka,trade服务,且数据库存在junit_test_sysconfig_set配置
	 * @method getSysConfigValueTest 
	 * @return void
	 * @date 2018年8月24日 下午1:38:00
	 */
	@Test
	public void getSysConfigValueTest(){
		String sysConfigValue = sysConfigClient.getSysConfigValue("junit_test_sysconfig_set");
		assertTrue(sysConfigValue != null);
		String sysConfigValueNull = sysConfigClient.getSysConfigValue("junit_test_sysconfig_set_null");
		assertTrue(sysConfigValueNull == null);
		assertFalse("".equals(sysConfigValueNull));
	}
	
	/**
	 * @author WangWei
	 * @Description 测试设置配置参数,需要先启动eureka,trade服务,且数据库存在junit_test_sysconfig_set配置
	 * @method setSysConfigValueTest 
	 * @return void
	 * @date 2018年8月24日 下午1:46:29
	 */
	@Test
	public void setSysConfigValueTest(){
		String value = "" + System.currentTimeMillis();
		boolean setSysConfigValue = Boolean.parseBoolean(sysConfigClient.setSysConfigValue("junit_test_sysconfig_set", value ));
		assertTrue(setSysConfigValue);
		assertEquals(value, sysConfigClient.getSysConfigValue("junit_test_sysconfig_set"));
		boolean setSysConfigValue2 = Boolean.parseBoolean(sysConfigClient.setSysConfigValue("junit_test_sysconfig_set_null", value ));
		assertFalse(setSysConfigValue2);
	}
	/**
	 * @author WangWei
	 * @Description 测试判断配置key是否存在,需要先启动eureka,trade服务,且数据库存在junit_test_sysconfig_set配置
	 * @method isKeyExistTest 
	 * @return void
	 * @date 2018年8月24日 下午1:47:27
	 */
	@Test
	public void isKeyExistTest(){
		boolean keyExist = Boolean.parseBoolean(sysConfigClient.isKeyExist("junit_test_sysconfig_set"));
		assertTrue(keyExist);
		boolean nullKey = Boolean.parseBoolean(sysConfigClient.isKeyExist("junit_test_sysconfig_nullKey"));
		assertTrue(!nullKey);
	}
}
