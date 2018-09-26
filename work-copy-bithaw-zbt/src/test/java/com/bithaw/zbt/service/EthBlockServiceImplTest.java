/**
 * @Description EthBlockServiceImpl junit 测试文件
 * @author  WangWei
 * @Date    2018年8月27日  上午10:08:11
 * @version   V 1.0
 */
package com.bithaw.zbt.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bithaw.zbt.BithawZBTTradeApplication;

/**
 * @Description 
 * @author   WangWei EthBlockServiceImpl junit测试类
 * @date     2018年8月27日 上午10:08:11
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
@Transactional
public class EthBlockServiceImplTest {
	@Autowired
	private EthBlockService ethBlockServicel;
	
	@Test
	public void scanBlockLocalTask() throws InterruptedException{
		ethBlockServicel.scanBlockLocalTask();
		Thread.sleep(3000000L);
	}
	@Test
	public void scanBlockEtherscanAsync() throws InterruptedException{
		ethBlockServicel.scanBlockEtherscanAsync();
		Thread.sleep(3000000L);
	}
}
