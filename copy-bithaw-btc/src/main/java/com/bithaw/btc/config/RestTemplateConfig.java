package com.bithaw.btc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description RestTemplate配置类
 * @author   WangWei
 * @date     2018年8月31日 下午3:55:43
 * @version  V 1.0
 */
@Configuration
public class RestTemplateConfig {
	@Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(20000);
        factory.setConnectTimeout(20000);
        return factory;
    }
	@Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }
}