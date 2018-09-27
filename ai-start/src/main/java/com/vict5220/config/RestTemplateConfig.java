/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月27日  下午8:02:34
 * @version   V 1.0
 */
package com.vict5220.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月27日 下午8:02:34
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
