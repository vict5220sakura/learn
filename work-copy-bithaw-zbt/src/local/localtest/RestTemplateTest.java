package localtest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.feign.SysConfigClient;

@SpringBootTest(classes = BithawZBTTradeApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RestTemplateTest {
	@Autowired
	private RestTemplate restTemplate; 
	@Autowired
	private SysConfigClient sysConfigClient;
	@Test
	public void etherscanTest(){
		String etherscanApikey = sysConfigClient.getSysConfigValue("etherscan_apikey");
		String etherscanUrl = sysConfigClient.getSysConfigValue("etherscan_url");
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "proxy");
		map.add("action", "eth_sendRawTransaction");
		map.add("hex", "0xf8aa45850165a0bc008307a12094b00ecbd39b5138f9eb7680205f565848b369974280b844a9059cbb0000000000000000000000004fe415ddf8451c9de3f89d8f815f0d014fbe567b00000000000000000000000000000000000000000000000000000000000000011ba07f9cea3a05a47ae4e00c7cb6d27b46f43b18682e4975cc919fbbe5dfc3bda2d2a01e16a83358e7a6259b964477fecde60b9e1124828388df9d7303c291f05344d0");
		map.add("apikey", etherscanApikey);
		HttpHeaders headers = new HttpHeaders();
		//  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity(etherscanUrl, request, JSONObject.class);
		System.out.println(postForEntity.getBody().toJSONString());
	}
	
	@Test
	public void etherscanTestgetAddressTxCount(){
		String etherscanApikey = "DVQNVEXB5VU6DRQQ78YXS6AZIQACX649ZV";
		String etherscanUrl = "https://api.etherscan.io/api";
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "proxy");
		map.add("action", "eth_getTransactionCount");
		map.add("address", "0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		map.add("apikey", etherscanApikey);
		map.add("tag", "latest");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<Object> postForEntity = restTemplate.postForEntity(etherscanUrl, request, Object.class);
		System.out.println();
//		String hex = postForEntity.getBody().getString("result");
//		Long parseLong = Long.parseLong(hex.substring(2), 16);
//		int nonce = parseLong.intValue();
	}
}
