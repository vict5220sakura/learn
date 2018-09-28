/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月28日  上午10:42:38
 * @version   V 1.0
 */
package com.vict5220.util;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月28日 上午10:42:38
 * @version  V 1.0
 */
public class TencentAiSignSortTest {
	
	@Test
	public void getSignature() throws IOException{
		HashMap<String, String> map = new HashMap<String,String>();
		map.put("app_id", "10000");
		map.put("time_stamp", "1493449657");
		map.put("nonce_str", "20e3408a79");
		map.put("key1", "腾讯AI开放平台");
		map.put("key2", "示例仅供参考");
		String signature = TencentAISignSort.getSignature(map, "a95eceb1ac8c24ee28b70f7dbba912bf");
		System.out.println(signature);
	}
}
