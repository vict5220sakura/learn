/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月27日  下午8:06:44
 * @version   V 1.0
 */
package com.vict5220.util;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

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
	
	public Optional<String> translate(String text) throws IOException{
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("app_id", APIID);
		map.add("time_stamp", System.currentTimeMillis() / 1000L + "");
		map.add("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
		map.add("type", "1");
		map.add("text", text);
		String sign = TencentAISignSort.getSignature(map.toSingleValueMap(), APIKEY);
		map.add("sign", sign);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> postForEntity = restTemplate.postForEntity(APIURL, request, String.class);
		Optional<String> optional = Optional.ofNullable(postForEntity).map(o -> o.getBody());
		return optional;
	}
}
