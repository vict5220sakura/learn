/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月26日  下午4:21:48
 * @version   V 1.0
 */
package com.soft;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * @Description
 * @author WangWei
 * @date 2018年9月26日 下午4:21:48
 * @version V 1.0
 */
@SpringBootApplication
public class MainApplication {
	public static void main(String[] args) throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApplication.class);
		builder.headless(false).run(args);
		// MyGuiFrame swing = applicationContext.getBean(MyGuiFrame.class);
		// swing.setVisible(true);
	}
}
