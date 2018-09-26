package com.bithaw.zbt.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bithaw.zbt.service.EthBlockService;

import lombok.extern.slf4j.Slf4j;
/**
 * @Description 初始化EthBlockService
 * @author   WangWei
 * @date     2018年8月24日 上午11:10:51
 * @version  V 1.0
 */
@Slf4j
@Component
@Order(value = 1)
public class EthBlockServiceInit implements ApplicationRunner{
	@Autowired
	EthBlockService ethBlockService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("初始化 开始");
		ethBlockService.init();
		log.info("初始化 结束");
	}

}
