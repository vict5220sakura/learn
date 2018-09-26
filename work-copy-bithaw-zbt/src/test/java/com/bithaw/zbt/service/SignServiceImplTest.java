/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月27日  下午4:49:41
 * @version   V 1.0
 */
package com.bithaw.zbt.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.common.utils.Rsa2Sign;
import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.entity.EthTradeNonce;
import com.bithaw.zbt.mapper.EthTradeNonceMapper;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月27日 下午4:49:41
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
@Transactional
public class SignServiceImplTest {
	
	@Autowired
	private EthTradeNonceMapper ethTradeNonceMapper;
	@Autowired
	private SignService signService;
	
	@Before
	public void before(){
		EthTradeNonce ethTradeNonce = new EthTradeNonce.Builder()//
				.setOrderNo("test_OrderNo2")//
				.setFromAddress("test_fromAddress2")//
				.setToAddress("test_ToAddress")//
				.setValue(new BigDecimal("0"))//
				.setData("TEST_data2")//
				.setNonce(1)//
				.setGasLimit(0L)//
				.setGasPrice(new BigDecimal("0"))//
				.setTxhash("test_Txhash2")//
				.setCreateTime(new Date())//
				.setState(0)//
				.build();
		ethTradeNonceMapper.save(ethTradeNonce);
	}
	
	/**
	 * @author WangWei
	 * @Description 测试需要手动更改适当的publicKey
	 * @method getAwaitDataTest 
	 * @return void
	 * @throws Exception 
	 * @date 2018年8月27日 下午4:52:01
	 */
	@Test
	public void getAwaitDataTest() throws Exception{
		String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJg4y/bi17nOvMwOyaerqjmJTnTTsx6qZqow200JXuOfG8WTDpDbR7YM6N2oal9dz2/RgqNuTm9qOaL2sif7gK0CAwEAAQ==";
		JSONObject awaitData = signService.getAwaitData();
		System.out.println(awaitData.toJSONString());
		String dataStr = awaitData.getJSONArray("data").toJSONString();
		boolean verify = Rsa2Sign.verify(dataStr, awaitData.getString("sign"), publicKey);
		assertTrue(verify);
		assertTrue(awaitData.getJSONArray("data").size()==1);
		assertTrue(awaitData.getJSONArray("data").getJSONObject(0).getString("nonce").equals("1"));
		BigDecimal gasPrice = new BigDecimal(awaitData.getJSONArray("data").getJSONObject(0).getString("gasPrice"));
		assertTrue(gasPrice.equals(new BigDecimal("0")));
		assertTrue(awaitData.getJSONArray("data").getJSONObject(0).getString("gasLimit").equals("0"));
		assertTrue(awaitData.getJSONArray("data").getJSONObject(0).getString("to").equals("test_ToAddress"));
		BigDecimal value = new BigDecimal(awaitData.getJSONArray("data").getJSONObject(0).getString("value"));
		assertTrue(value.equals(new BigDecimal("0")));
		assertTrue(awaitData.getJSONArray("data").getJSONObject(0).getString("data").equals("TEST_data2"));
	}
}
