package com.vict5220;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class EurekaClientRibbonApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(EurekaClientRibbonApplication.class, args);
	}
	
	@Bean
    @LoadBalanced//开启RestTemplate负载均衡
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
