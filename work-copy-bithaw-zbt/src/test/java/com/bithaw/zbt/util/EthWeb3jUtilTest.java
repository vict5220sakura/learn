/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  下午5:02:10
 * @version   V 1.0
 */
package com.bithaw.zbt.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bithaw.zbt.utils.eth.EthWeb3jUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月11日 下午5:02:10
 * @version  V 1.0
 */
@Slf4j
public class EthWeb3jUtilTest {
	@Test
	public void newEthAccountTest() throws Exception{
		String newEthAccount = EthWeb3jUtil.newEthAccount("123");
		assertTrue(newEthAccount.split("\\|").length == 2);
	}
}
