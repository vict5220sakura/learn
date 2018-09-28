/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  下午5:25:48
 * @version   V 1.0
 */
package com.bithaw.btc.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bithaw.btc.utils.BlockchainApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月31日 下午5:25:48
 * @version  V 1.0
 */
@Slf4j
@Component
@Order(value = 1)
public class BlockchainApiUtilsInit implements ApplicationRunner{
	@Autowired
	private BlockchainApiUtils blockchainApiUtils;
	
	/** 
	 * <p>Title: run</p>
	 * <p>Description: </p>
	 * @param args
	 * @throws Exception
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)  
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("初始化");
		blockchainApiUtils.init();
		log.info("初始化,结束");
	}
}
