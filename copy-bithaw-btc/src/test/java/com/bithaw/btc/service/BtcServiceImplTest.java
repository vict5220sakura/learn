/**
 * @Description BtcServiceImpl junit测试文件
 * @author  WangWei
 * @Date    2018年8月28日  上午10:46:20
 * @version   V 1.0
 */
package com.bithaw.btc.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.BithawBtcApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description BtcServiceImpl junit测试类
 * @author   WangWei
 * @date     2018年8月28日 上午10:46:20
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
@Slf4j
public class BtcServiceImplTest {
	@Autowired
	private BtcWalletService btcWalletService;
	
	@Before
	public void before(){
		
	}
	
	@Test
	public void newAddressTest(){
		String newAddress = btcWalletService.newAddress();
		assertTrue(newAddress.split("\\|").length == 2);//生成公钥私钥分割的字符串
		assertTrue(newAddress.split("\\|")[0].length() == 52);//私钥52个字符
		assertTrue(newAddress.split("\\|")[1].length() == 34);//公钥34个字符
	}
	
	/**
	 * @author WangWei
	 * @Description 需要自行查看交易后的返回值
	 * @method tradeTest
	 * @throws Throwable 
	 * @return void
	 * @date 2018年8月28日 上午10:52:15
	 */
	@Test
	public void tradeTest() throws Throwable{
		JSONObject trade = btcWalletService.trade("mgd5gpprouttXnJkmcHqGYL7XV8fz2mEwB", 0.00001);
		log.info("交易后的hash:{}",trade);
	}
	
	/**
	 * @author WangWei
	 * @Description 需要手动指定交易hash
	 * @method getTradeStatusTest
	 * @throws Throwable 
	 * @return void
	 * @date 2018年8月28日 上午10:54:27
	 */
	@Test
	public void getTradeStatusTest() throws Throwable{
		String tradeStatus = btcWalletService.getTradeStatus("d132f38ffd158bb346862f5a7773b5ac8f23a52c87b60be8ea47cbbb0797ba3d");
		assertTrue(tradeStatus.equals("TRADING"));
	}
	
	/**
	 * @author WangWei
	 * @Description 获取交易详细信息
	 * @method getTradeInfoTest
	 * @throws Throwable 
	 * @return void
	 * @date 2018年8月28日 上午10:57:39
	 */
	@Test
	public void getTradeInfoTest() throws Throwable{
		JSONObject tradeInfo = btcWalletService.getTradeInfo("12286a3162b8536cc593b0d3dc0d8c84d1e3c5f54d5c1123b95da43fbf9b814b");
		log.info("手动查看交易详细信息json字符串:{},{}",tradeInfo.toJSONString(),tradeInfo);
	}
	
	/**
	 * @author WangWei
	 * @Description 检查节点状态
	 * @method checkNodeTest 
	 * @return void
	 * @date 2018年8月28日 上午10:59:12
	 */
	@Test
	public void checkNodeTest(){
		boolean checkNode = btcWalletService.checkNode();
		assertTrue(checkNode);
	}
	
}
