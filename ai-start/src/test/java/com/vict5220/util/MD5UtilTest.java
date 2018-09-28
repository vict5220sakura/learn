/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月28日  上午9:28:13
 * @version   V 1.0
 */
package com.vict5220.util;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月28日 上午9:28:13
 * @version  V 1.0
 */
@Slf4j
public class MD5UtilTest {
	
	@Test
	public void getStringMD5(){
		String stringMD5 = MD5Util.getStringMD5("23");
		log.info("测试 {}", stringMD5);
	}
}
