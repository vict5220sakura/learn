/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  下午1:31:03
 * @version   V 1.0
 */
package com.bithaw.btc.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bithaw.btc.utils.BitRestUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 初始化BitRestUtils
 * @author   WangWei
 * @date     2018年8月31日 下午1:31:03
 * @version  V 1.0
 */
@Slf4j
@Component
@Order(value = 1)
public class BitRestUtilsInit  implements ApplicationRunner{
	@Autowired
	private BitRestUtils bitRestUtilsl;

	/** 
	 * <p>Title: run</p>
	 * <p>Description: </p>
	 * @param args
	 * @throws Exception
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)  
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("初始化开始");
		bitRestUtilsl.init();
		log.info("初始化结束");
	}
}
