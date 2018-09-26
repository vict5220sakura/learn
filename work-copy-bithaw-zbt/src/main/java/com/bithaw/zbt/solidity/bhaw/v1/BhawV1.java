/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午2:59:44
 * @version   V 1.0
 */
package com.bithaw.zbt.solidity.bhaw.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description V1合约
 * @author   WangWei
 * @date     2018年9月12日 下午2:59:44
 * @version  V 1.0
 */
@Slf4j
@Component
public class BhawV1 {
	
	@Autowired
	public BhawV1CreateTransactionData bhawV1CreateTransactionData;
	
	@Autowired
	public BhawV1SelectByWeb3j bhawV1SelectByWeb3j;
	
	@Autowired
	public BhawV1SelectByEtherscan bhawV1SelectByEtherscan;
	
	/**
	 * solidityAddress : 合约地址
	 */
	public static final String solidityAddress = "0xb00ecbd39b5138f9eb7680205f565848b3699742";

	/**
	 * @author WangWei
	 * @Description 初始化
	 * @method init void
	 * @date 2018年9月12日 下午3:02:13
	 */
	public void init() {
		log.info("初始化");
	}
	
}
