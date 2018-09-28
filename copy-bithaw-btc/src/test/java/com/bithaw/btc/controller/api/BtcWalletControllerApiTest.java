/**
 * @Description BtcWalletControllerApi 比特币对内接口junit测试文件
 * @author  WangWei
 * @Date    2018年8月28日  上午11:04:33
 * @version   V 1.0
 */
package com.bithaw.btc.controller.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.BithawBtcApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description BtcWalletControllerApi 比特币对内接口junit测试类
 * @author   WangWei
 * @date     2018年8月28日 上午11:04:33
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
@Slf4j
public class BtcWalletControllerApiTest {
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
	@Before // 这个方法在每个方法执行之前都会执行一遍
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); // 初始化MockMvc对象
	}
	
	@Value("${bithaw.feign.token}")
	private String token;
	
	/**
	 * @author WangWei
	 * @Description 测试新建账户
	 * @method newAddressTest
	 * @throws Exception 
	 * @return void
	 * @date 2018年8月28日 上午11:11:35
	 */
	@Test
	public void newAddressTest() throws Exception{
		RequestBuilder request;
		request = post("/btc/newAddress")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("rpc-token",token)
				.accept(MediaType.TEXT_PLAIN);
		String contentAsString = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		log.info("注册的地址为{}",contentAsString);
		assertTrue(contentAsString.split("\\|").length == 2);//生成公钥私钥分割的字符串
		assertTrue(contentAsString.split("\\|")[0].length() == 52);//私钥52个字符
		assertTrue(contentAsString.split("\\|")[1].length() == 34);//公钥34个字符
	}
	
	/**
	 * @author WangWei
	 * @Description 测试转账,需要手动查看区块链交易信息
	 * @method tradeTest 
	 * @return void
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 * @date 2018年8月28日 上午11:12:20
	 */
	@Test
	public void tradeTest() throws UnsupportedEncodingException, Exception{
		RequestBuilder request;
		request = post("/btc/trade")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("toAddress", "1Q3sRKNZpZsuHPKUmyPo92k1Es4qxNQuLN")
				.param("amount", "0.00001")
				.header("rpc-token",token)
				.accept(MediaType.TEXT_PLAIN);
		String contentAsString = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		JSONObject parseObject = JSONObject.parseObject(contentAsString);
		assertTrue(parseObject.getString("code").equals("0"));
		assertTrue(parseObject.getString("txHash").length() == 64);
	}
	
	/**
	 * @author WangWei
	 * @Description 测试获取交易状态,需要手动设置txHash参数
	 * @method getTradeStatusTest 
	 * @return void
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 * @date 2018年8月28日 上午11:25:33
	 */
	@Test
	public void getTradeStatusTest() throws UnsupportedEncodingException, Exception{
		RequestBuilder request;
		request = post("/btc/getTradeStatus")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("txHash", "ad21161ac9ac657309e365c577ce7475d69722101090dc9b0f9745e02386e025")
				.header("rpc-token",token)
				.accept(MediaType.TEXT_PLAIN);
		String contentAsString = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		assertTrue(contentAsString.equals("TRADING"));
	}
	
	/**
	 * @author WangWei
	 * @Description 查询交易状态详细信息,需要保证交易已经被确认
	 * @method getTradeInfoTest 
	 * @return void
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 * @date 2018年8月28日 上午11:27:56
	 */
	@Test
	public void getTradeInfoTest() throws UnsupportedEncodingException, Exception{
		RequestBuilder request;
		request = post("/btc/getTradeStatusInfo")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("txHash", "993a91c6e0c4ec2eccadc7381cb89dbaec65018b325729878491c78657afd695")
				.header("rpc-token",token)
				.accept(MediaType.TEXT_PLAIN);
		String contentAsString = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		log.info("手动查看json字符串判断正误,json字符串:{}",contentAsString);
	}
	
	/**
	 * @author WangWei
	 * @Description 检查节点状态,查看数据库配置表变化 btc_node_state 字段状态
	 * @method checkNode 
	 * @return void
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 * @date 2018年8月28日 上午11:34:11
	 */
	@Test
	public void checkNode() throws UnsupportedEncodingException, Exception{
		RequestBuilder request;
		request = post("/btc/checkNode")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("rpc-token",token)
				.accept(MediaType.TEXT_PLAIN);
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andDo(print())
				;
	}
}
