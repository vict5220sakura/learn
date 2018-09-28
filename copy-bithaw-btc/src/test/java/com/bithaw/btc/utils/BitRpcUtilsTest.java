/**
 * @Description BitRpcUtils junit测试文件
 * @author  WangWei
 * @Date    2018年8月28日  上午9:22:30
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description BitRpcUtils junit测试类 需要开启eureka和trade,不同节点测试需要根据不同节点配置
 * @author   WangWei
 * @date     2018年8月28日 上午9:22:30
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
@Slf4j
public class BitRpcUtilsTest {
	@Autowired
	private BitRpcUtils bitRpcUtils;
	
	@Before
	public void before(){
		
	}
	
	/**
	 * @author WangWei
	 * @Description 新建账号,私钥|公钥
	 * @method newAddressTest 
	 * @return void
	 * @date 2018年8月28日 上午9:59:26
	 */
	@Test
	public void newAddressTest(){
		String newAddress = bitRpcUtils.newAddress();
		assertTrue(newAddress.split("\\|").length == 2);//生成公钥私钥分割的字符串
		assertTrue(newAddress.split("\\|")[0].length() == 52);//私钥52个字符
		assertTrue(newAddress.split("\\|")[1].length() == 34);//公钥34个字符
	}
	
	/**
	 * @author WangWei
	 * @Description 合法性校验,需要手动输入正确的公钥与错误的公钥
	 * @method checkAddressTest 
	 * @return void
	 * @date 2018年8月28日 上午9:59:53
	 */
	@Test
	public void checkAddressTest(){
		boolean checkAddressTrue = bitRpcUtils.checkAddress("1FCa1GWVGEU8unrkyXhHoxwjnHXAF7cHBQ");
		boolean checkAddressFalse = bitRpcUtils.checkAddress("moYKxABmKjQtiju2KAdK8XrsbQ1dMmzzxu");
		assertTrue(checkAddressTrue);
		assertFalse(checkAddressFalse);
	}
	
	/**
	 * @author WangWei
	 * @Description 准确信息需要手动观察
	 * @method getBlockHeightTest 
	 * @return void
	 * @throws Throwable 
	 * @date 2018年8月28日 上午10:05:19
	 */
	@Test
	public void getBlockHeightTest() throws Throwable{
		Integer blockHeight = bitRpcUtils.getBlockHeight();
		assertTrue(blockHeight > 0);
		log.info("当前区块最高块{}",blockHeight);
	}
	
	/**
	 * @author WangWei
	 * @Description 解锁比特币钱包,需要设置钱包密码与查看
	 * @method walletpassphaseTest 
	 * @return void
	 * @date 2018年8月28日 上午10:28:03
	 */
	@Test
	public void walletpassphaseTest(){
		boolean walletpassphase = bitRpcUtils.walletpassphase("ashjduncxk5as4d1DSJDKHasd4", 60);
		assertTrue(walletpassphase);
		boolean walletpassphase2 = bitRpcUtils.walletpassphase("ashjduncxk5as4d1DSJDKHasd42", 60);
		assertFalse(walletpassphase2);
	}
	
	/**
	 * @author WangWei
	 * @Description 获取钱包余额,需要手动设置余额
	 * @method getBalanceTest
	 * @throws Throwable 
	 * @return void
	 * @date 2018年8月28日 上午10:32:47
	 */
	@Test
	public void getBalanceTest() throws Throwable{
		double balance = bitRpcUtils.getBalance();
		assertTrue(balance > 0D);
		assertTrue(balance == 2.22733952D);
	}
	
	/**
	 * @author WangWei
	 * @Description 获取节点链接数
	 * @method getConnectionCountTest 
	 * @return void
	 * @date 2018年8月28日 上午10:35:16
	 */
	@Test
	public void getConnectionCountTest(){
		Integer connectionCount = bitRpcUtils.getConnectionCount();
		assertTrue(connectionCount > 0);
		assertTrue(connectionCount > 3);
	}
	
	/**
	 * @author WangWei
	 * @Description 转账
	 * @method tradeTest 
	 * @return void
	 * @throws Throwable 
	 * @date 2018年8月28日 上午10:37:07
	 */
	@Test
	public void tradeTest() throws Throwable{
		boolean walletpassphase = bitRpcUtils.walletpassphase("5462", 60);
		assertTrue(walletpassphase);
		String txHash = bitRpcUtils.trade("mgd5gpprouttXnJkmcHqGYL7XV8fz2mEwB",0.00001);
		log.info("根据交易hash到区块链上查找是否交易成功!交易hash: {}",txHash);
	}
}
