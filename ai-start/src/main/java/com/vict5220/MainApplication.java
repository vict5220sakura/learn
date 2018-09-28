/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月27日  下午8:02:01
 * @version   V 1.0
 */
package com.vict5220;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import cn.xsshome.taip.sign.TencentAISignSort;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月27日 下午8:02:01
 * @version  V 1.0
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class MainApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApplication.class, args);
	}
}
