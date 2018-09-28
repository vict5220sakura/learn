/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月27日  下午8:16:06
 * @version   V 1.0
 */
package com.vict5220.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.mail.URLName;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @author WangWei
 * @date 2018年9月27日 下午8:16:06
 * @version V 1.0
 */
@Slf4j
public class SignUtil {
	public static String signMd5(Map<String, String> map, String apikey) {
		ArrayList<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);

		String sign = "";
		for (String key : list) {
			sign += key;
			sign += "=";
			try {
				sign += URLEncoder.encode(map.get(key), "UTF-8").toUpperCase();
			} catch (Exception e) {
				log.error("计算签名URL编码错误,请检查", e);
			}
			sign += "&";
		}
		sign += "app_key";
		sign += "=";
		sign += apikey;

		return MD5Util.getStringMD5(sign).toUpperCase();
	}
}
