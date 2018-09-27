/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月27日  下午8:06:44
 * @version   V 1.0
 */
package com.vict5220.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月27日 下午8:06:44
 * @version  V 1.0
 */
@Component
public class TextAiUtil {
	private final String APIURL = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttrans";
	private final String APIKEY = "3b0TsY8DvNGC0h4c";
	private final String APIID = "2108614417";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Optional<JSONObject> translate(String text){
		Optional<JSONObject> empty = Optional.empty();
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "proxy");
		map.add("action", "eth_call");
		map.add("tag", "latest");
		
		SignUtil.signMd5(map, APIKEY);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> postForEntity = restTemplate.postForEntity(etherscanUrl, request, String.class);
		
		return empty;
	}
}
