package com.bithaw.btc.amytest;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestJSONArray {
	
	@Test
	public void testJSONArrayTest(){
		JSONArray jsonArray = JSONObject.parseArray("[\"asd\",\"123\"]");
		System.out.println(jsonArray.contains("asd1"));
	}
}
