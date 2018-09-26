/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  下午2:27:20
 * @version   V 1.0
 */
package com.bithaw.zbt.solidity.bhaw.v1;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.zbt.BithawZBTTradeApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月11日 下午2:27:20
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class BhawV1CreateTransactionDataTest {
	@Autowired
	private BhawV1CreateTransactionData bhawV1CreateTransactionData;
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method transferOwnershipTest 
	 * @return void
	 * @date 2018年9月11日 下午2:29:19
	 */
	@Test
	public void transferOwnershipTest(){
		//注意测试移交管理员权限地址必须拥有且正确
		//注意测试移交管理员权限地址必须拥有且正确
		//注意测试移交管理员权限地址必须拥有且正确
		//注意测试移交管理员权限地址必须拥有且正确
		Optional<String> data = bhawV1CreateTransactionData.transferOwnership("0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b");
		assertTrue(data.isPresent());
		log.info(data.toString());
	}
	
	@Test
	public void transferTest(){
		Optional<String> data = bhawV1CreateTransactionData.transfer("0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b", new BigInteger("100000"));
		assertTrue(data.isPresent());
		log.info(data.toString());
	}
	
	@Test
	public void transferFromTest(){
		Optional<String> data = bhawV1CreateTransactionData.transferFrom("0x763b78bc83ef328a252b25b56ba2a7fe49774b72","0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b", new BigInteger("100000"));
		assertTrue(data.isPresent());
		log.info(data.toString());
	}
	
	@Test
	public void approveTest(){
		Optional<String> data = bhawV1CreateTransactionData.approve("0x763b78bc83ef328a252b25b56ba2a7fe49774b72", new BigInteger("100000"));
		assertTrue(data.isPresent());
		log.info(data.toString());
	}
	@Test
	public void burnTest(){
		Optional<String> data = bhawV1CreateTransactionData.burn( new BigInteger("100000"));
		assertTrue(data.isPresent());
		log.info(data.toString());
	}
	@Test
	public void freezeAccountTest(){
		Optional<String> data = bhawV1CreateTransactionData.freezeAccount( "0x763b78bc83ef328a252b25b56ba2a7fe49774b72",true );
		assertTrue(data.isPresent());
		log.info(data.toString());
	}
	
	
}
