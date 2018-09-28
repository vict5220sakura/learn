package com.bithaw.btc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis基础配置
 *
 * @author 
 * @date
 */
@Configuration
@MapperScan("com.bithaw.btc.mapper")
public class MyBatisConfig {
}
