/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月28日  上午9:58:01
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

import com.vict5220.MainApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月28日 上午9:58:01
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class TextAiUtilTest {
	@Autowired
	private TextAiUtil textAiUtil;
	
	@Test
	public void translate() throws IOException{
		Optional<String> translate = textAiUtil.translate("接口调用示例");
		log.info("测试 {}", translate);
	}
}
