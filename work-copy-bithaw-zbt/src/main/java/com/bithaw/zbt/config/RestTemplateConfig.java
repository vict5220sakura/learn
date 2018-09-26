package com.bithaw.zbt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description 访问第三方接口RestTemplate工具
 * @author   WangWei
 * @date     2018年8月24日 上午11:04:10
 * @version  V 1.0
 */
@Configuration
public class RestTemplateConfig {
	@Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(20000);
        factory.setReadTimeout(10000);
        return factory;
    }
	@Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }
}
