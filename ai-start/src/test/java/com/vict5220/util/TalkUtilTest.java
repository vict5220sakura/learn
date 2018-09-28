/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月28日  下午5:40:03
 * @version   V 1.0
 */
package com.vict5220.util;

import java.io.IOException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.vict5220.MainApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月28日 下午5:40:03
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class TalkUtilTest {
	@Autowired
	private TalkUtil talkUtil;
	
	@Test
	public void talk() throws IOException{
		Optional<String> talk = talkUtil.talk("你好", "10000");
		JSONObject json = JSONObject.parseObject(talk.orElseGet(() -> ""));
		Optional<JSONObject> jsonO = Optional.ofNullable(json);
		String answer = jsonO.map(o -> o.getJSONObject("data")).map(o -> o.getString("answer")).orElseGet(() -> "系统错误,请稍后重试");
		log.info("测试 {}", answer);
	}
}
