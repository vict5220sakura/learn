/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午2:35:19
 * @version   V 1.0
 */
package com.bithaw.zbt.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bithaw.zbt.utils.eth.EtherscanApiUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月12日 下午2:35:19
 * @version  V 1.0
 */
@Slf4j
@Component
@Order(value = 1)
public class EtherscanApiUtilInit implements ApplicationRunner{
	@Autowired 
	private EtherscanApiUtil etherscanApiUtil;

	/** 
	 * <p>Title: run</p>
	 * <p>Description: </p>
	 * @param args
	 * @throws Exception
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)  
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		etherscanApiUtil.init();
	}
	
}
