/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月14日  上午9:31:15
 * @version   V 1.0
 */
package com.vict5220.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月14日 上午9:31:15
 * @version  V 1.0
 */
@Slf4j
@RestController
public class LogTestController {
	
	@RequestMapping("test")
	public String test(HttpServletRequest request){
		log.info("本地日志:{}",request.getLocalName());//输出到控制台
		log.warn("测试warn");
		log.error("测试error");
		return "haha";
	}
}
