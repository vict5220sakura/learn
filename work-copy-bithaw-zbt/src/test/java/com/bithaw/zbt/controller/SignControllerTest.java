/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月27日  下午5:21:19
 * @version   V 1.0
 */
package com.bithaw.zbt.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.common.utils.Rsa2Sign;
import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.entity.EthTradeNonce;
import com.bithaw.zbt.mapper.EthTradeNonceMapper;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月27日 下午5:21:19
 * @version  V 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=BithawZBTTradeApplication.class)
@WebAppConfiguration
@Transactional
public class SignControllerTest {
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
	@Before // 这个方法在每个方法执行之前都会执行一遍
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); // 初始化MockMvc对象
	}
	
	@Autowired
	private EthTradeNonceMapper ethTradeNonceMapper;
	
	@Before
	public void insertTestData(){
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
	 * @Description 测试需要手动指定私钥
	 * @method getAwaitDataTest 
	 * @return void
	 * @throws Exception 
	 * @date 2018年8月27日 下午5:25:41
	 */
	@Test
	public void getAwaitDataTest() throws Exception{
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJZBecO2bOF1fFNmu55KPoWpuhgCsQtjB4U/CUISkDW6/R+olHdNxTLHgr/Y2RoCgS1dTs43/Y2amnd42nTQ3uB5LzyRrgr51RM7XwHZ2qCcfe540+SpTKhqNZAVkrB2KR6g5qxEFBLnR7H2TLUzC0VUGr5h59VNd9A5RRPW/dVDAgMBAAECgYAPPntVENgBE8NWTtDwIUYwl2Sq9PLzXcuwiBGvY2TAHsV5hcfyRrCgEz+/qQd1rRVf/dx17ZZK3ImZX2iCe4JxCYjyR0/0kMPkXOlqcHyA6jfSCMrFBkZuJboMhb9zplS8Aaoaiix3li7LYN8UoCfYY8iRZS+LMO0Us5nkDldcoQJBAOHcFhdt/WGdn3dnJyBII8hbr5OVgu0GpPOYkgttYcbf4dL1UtBbXT+8qjNWY/NDkD9tR+ClOqSUW9WzfIeIe1MCQQCqTpNjy2WLQybiborp+LHvBLR1BI8M9MvmG4n2veAnW++/HYDeEFl0oPRySGJzUPIyokB+/avGaLl5TK+E6/BRAkEAtnpPauyVe8mSrjCsHtvJ9TWGXFG+buwgVyMcU5kzFy+Izx0fpHE8nKM8S6/vnvomT+hl2y0DfTbUH4sdAI+XOwJABpcTzQDJ5SGsF4b/iR4+hGlCMmUCxBWU7kqShCHE0sET/ek+W1l2nLS9wM5cQOoY9SyiIkfepcbXpoH5KMOYgQJAdZPpcjHDbJdqOWd4Qc7Nc5ls83+pot00v+tG04/Y77wcoa+YP6LhjdBT555Gap70PeelaBU1cro5dxjVW1TDnA==";
		String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJg4y/bi17nOvMwOyaerqjmJTnTTsx6qZqow200JXuOfG8WTDpDbR7YM6N2oal9dz2/RgqNuTm9qOaL2sif7gK0CAwEAAQ==";
		String json = System.currentTimeMillis() + "";
		
		RequestBuilder request;
		request = post("/sign/getAwaitData")
				.param("json", json)
				.param("sign",Rsa2Sign.sign(json, privateKey))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.TEXT_PLAIN);
		String responseStr = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		JSONObject parseObject = JSONObject.parseObject(responseStr);
		String dataStr = parseObject.getJSONArray("data").toJSONString();
		boolean verify = Rsa2Sign.verify(dataStr, parseObject.getString("sign"), publicKey);
		assertTrue(verify);
		String string = parseObject.getJSONArray("data").getJSONObject(0).getString("orderNo");
		assertTrue(string.equals("test_OrderNo2"));
	}
	
	/**
	 * @author WangWei
	 * @Description 需要指定私钥
	 * @method setRawTransaction
	 * @throws Exception 
	 * @return void
	 * @date 2018年8月27日 下午6:05:18
	 */
	@Test
	public void setRawTransaction() throws  Exception{
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJZBecO2bOF1fFNmu55KPoWpuhgCsQtjB4U/CUISkDW6/R+olHdNxTLHgr/Y2RoCgS1dTs43/Y2amnd42nTQ3uB5LzyRrgr51RM7XwHZ2qCcfe540+SpTKhqNZAVkrB2KR6g5qxEFBLnR7H2TLUzC0VUGr5h59VNd9A5RRPW/dVDAgMBAAECgYAPPntVENgBE8NWTtDwIUYwl2Sq9PLzXcuwiBGvY2TAHsV5hcfyRrCgEz+/qQd1rRVf/dx17ZZK3ImZX2iCe4JxCYjyR0/0kMPkXOlqcHyA6jfSCMrFBkZuJboMhb9zplS8Aaoaiix3li7LYN8UoCfYY8iRZS+LMO0Us5nkDldcoQJBAOHcFhdt/WGdn3dnJyBII8hbr5OVgu0GpPOYkgttYcbf4dL1UtBbXT+8qjNWY/NDkD9tR+ClOqSUW9WzfIeIe1MCQQCqTpNjy2WLQybiborp+LHvBLR1BI8M9MvmG4n2veAnW++/HYDeEFl0oPRySGJzUPIyokB+/avGaLl5TK+E6/BRAkEAtnpPauyVe8mSrjCsHtvJ9TWGXFG+buwgVyMcU5kzFy+Izx0fpHE8nKM8S6/vnvomT+hl2y0DfTbUH4sdAI+XOwJABpcTzQDJ5SGsF4b/iR4+hGlCMmUCxBWU7kqShCHE0sET/ek+W1l2nLS9wM5cQOoY9SyiIkfepcbXpoH5KMOYgQJAdZPpcjHDbJdqOWd4Qc7Nc5ls83+pot00v+tG04/Y77wcoa+YP6LhjdBT555Gap70PeelaBU1cro5dxjVW1TDnA==";
		RequestBuilder request;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderNo", "test_OrderNo2");
		jsonObject.put("rawTransaction", "123");
		request = post("/sign/setRawTransaction")
				.param("json", jsonObject.toJSONString())
				.param("sign",Rsa2Sign.sign(jsonObject.toJSONString(), privateKey))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.TEXT_PLAIN);
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		EthTradeNonce findByorderNo = ethTradeNonceMapper.findByorderNo("test_OrderNo2");
		boolean equals = findByorderNo.getRawTransaction().equals("123");
		assertTrue(equals);
	}
}
