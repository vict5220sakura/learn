package com.bithaw.zbt.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;

/**
 * @author
 * @version
 */
@Configuration
public class ZBTConfiguration {

	// @Bean
	// public Web3jUtil web3j(){
	// return (Web3jUtil) Web3j.build(new
	// HttpService("http://47.52.190.231:30303/"));
	// }

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder().retryOnConnectionFailure(true)// 是否开启缓存
				.connectTimeout(10L, TimeUnit.MINUTES).readTimeout(10L, TimeUnit.MINUTES).build();
	}

}
