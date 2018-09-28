package com.bithaw.btc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * 交易中心BTC微服务启动类
 *
 * @author 
 * @date 
 */
@EnableCircuitBreaker
@EnableHystrix
@EnableHystrixDashboard
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class})
public class BithawBtcApplication {

    public static void main(String[] args) {
        SpringApplication.run(BithawBtcApplication.class, args);
    }
}
