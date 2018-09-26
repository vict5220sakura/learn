package com.bithaw.zbt.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bithaw.zbt.controller.api.EthBlockController;

import lombok.extern.slf4j.Slf4j;
/**
 * @Description 初始化ETHBlockSynchronizedController
 * @author   WangWei
 * @date     2018年8月24日 上午11:11:01
 * @version  V 1.0
 */
@Slf4j
@Component
@Order(value = 1)
public class EthBlockControllerInit implements ApplicationRunner{
	
	@Autowired
	EthBlockController ethBlockController;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("初始化 开始");
		ethBlockController.init();
		log.info("初始化 结束");
	}

}
