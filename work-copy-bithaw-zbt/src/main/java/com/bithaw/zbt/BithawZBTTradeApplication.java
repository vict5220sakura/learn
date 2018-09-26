package com.bithaw.zbt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * zbt微服务启动类
 *
 * @author 
 * @date 
 */
@EnableCircuitBreaker
@EnableHystrix
@EnableHystrixDashboard
@EnableFeignClients
@EnableDiscoveryClient
@EnableAutoConfiguration//(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class BithawZBTTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BithawZBTTradeApplication.class, args);
    }
}
