/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月7日  下午2:09:32
 * @version   V 1.0
 */
package com.bithaw.btc.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.BithawBtcApplication;
import com.bithaw.btc.utils.BtcComApiUtilsTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月7日 下午2:09:32
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class RestTemplateTest {
	@Autowired
	RestTemplate restTemplate;
	
	@Test
	public void getTest(){
		ResponseEntity<String> forEntity = restTemplate.getForEntity("https://chain.api.btc.com/v3/tx/485ed0a9b90b75c4b345f4c2fabe00c1ed5fc3ace608a6f0ac177af4ed48e0e1?verbose=3", String.class);
		System.out.println(forEntity.getBody());
		JSONObject parseObject = JSONObject.parseObject(forEntity.getBody());
		System.out.println(parseObject.toJSONString());
	}
}
