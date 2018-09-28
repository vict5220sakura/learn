/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月28日  下午5:44:43
 * @version   V 1.0
 */
package com.vict5220.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.vict5220.util.TalkUtil;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月28日 下午5:44:43
 * @version  V 1.0
 */
@Controller
public class TalkController {
	
	@Autowired
	private TalkUtil talkUtil;
	
	@RequestMapping("/")
	public String index(){
		return "index";
	}
	
	@ResponseBody
	@PostMapping("/talk")
	public String talk(String question){
		try {
			System.out.println(question);
			Optional<String> talk = talkUtil.talk(question, "10000");
			System.out.println(talk);
			JSONObject json = JSONObject.parseObject(talk.orElseGet(() -> ""));
			Optional<JSONObject> jsonO = Optional.ofNullable(json);
			String answer = jsonO.map(o -> o.getJSONObject("data")).map(o -> o.getString("answer")).orElseGet(() -> "系统错误,请稍后重试");
			return answer;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统错误请稍后重试";
		}
	}
}
