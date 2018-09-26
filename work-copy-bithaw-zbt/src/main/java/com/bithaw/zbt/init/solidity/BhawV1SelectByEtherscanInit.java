/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  上午10:30:01
 * @version   V 1.0
 */
package com.bithaw.zbt.init.solidity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bithaw.zbt.solidity.bhaw.v1.BhawV1SelectByEtherscan;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月12日 上午10:30:01
 * @version  V 1.0
 */
@Slf4j
@Component
@Order(value = 1)
public class BhawV1SelectByEtherscanInit implements ApplicationRunner{
	@Autowired
	private BhawV1SelectByEtherscan bhawV1SelectByEtherscan;
	
	/** 
	 * <p>Title: run</p>
	 * <p>Description: </p>
	 * @param args
	 * @throws Exception
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)  
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		bhawV1SelectByEtherscan.init();
	}

}
