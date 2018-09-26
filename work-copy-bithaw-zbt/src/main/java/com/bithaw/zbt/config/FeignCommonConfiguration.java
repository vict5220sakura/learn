package com.bithaw.zbt.config;

import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;

import org.springframework.context.annotation.Bean;

public class FeignCommonConfiguration {
	
	/**
	 * 设置日志级别
	 * @return
	 */
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
/*	 @Bean
	 public BasicAuthRequestInterceptor  requestInterceptor(){
	        return new BasicAuthRequestInterceptor("user1122","password2233");
	 }
	 */
}
