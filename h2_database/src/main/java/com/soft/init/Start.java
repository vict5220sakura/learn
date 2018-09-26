/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月26日  下午5:24:05
 * @version   V 1.0
 */
package com.soft.init;

import java.awt.Frame;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.soft.ui.util.MyGuiFrame;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月26日 下午5:24:05
 * @version  V 1.0
 */
@Slf4j
@Component
@Order(1)
public class Start implements ApplicationRunner{

	/** 
	 * <p>Title: run</p>
	 * <p>Description: </p>
	 * @param args
	 * @throws Exception
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)  
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//new MyGuiFrame().setVisible(true);
	}

}
