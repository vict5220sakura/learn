/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月28日  上午10:04:59
 * @version   V 1.0
 */
package com.vict5220.util;

import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月28日 上午10:04:59
 * @version  V 1.0
 */
@Slf4j
public class SignUtilTest {
	@Test
	public void signMd5(){
		HashMap<String, String> map = new HashMap<String,String>();
		map.put("app_id", "10000");
		map.put("time_stamp", "1493449657");
		map.put("nonce_str", "20e3408a79");
		map.put("key1", "腾讯AI开放平台");
		map.put("key2", "示例仅供参考");
		String signMd5 = SignUtil.signMd5(map, "a95eceb1ac8c24ee28b70f7dbba912bf");
		log.info("测试 {}", signMd5);
	}
}
