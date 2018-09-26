/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午6:59:25
 * @version   V 1.0
 */
package com.bithaw.zbt.mapper;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.entity.EthTradeNonceCover;

import lombok.extern.slf4j.Slf4j;


/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月12日 下午6:59:25
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class EthTradeNonceCoverMapperTest {
	
	@Autowired
	EthTradeNonceCoverMapper ethTradeNonceCoverMapper;
	
	@Test
	public void save(){
		ethTradeNonceCoverMapper.save(new EthTradeNonceCover().builder()
				.createTime(new Date())
				.data("测试")
				.fromAddress("测试")
				.gasLimit(123L)
				.gasPrice(new BigDecimal("1"))
				.nonce(1)
				.orderNo("测试")
				.rawTransaction("测试")
				.state(1)
				.toAddress("测试")
				.txhash("测试")
				.value(new BigDecimal("1")).build());
	}
	
	@Test
	public void findAllFinalStateNull(){
		List<EthTradeNonceCover> findAllFinalStateNull = ethTradeNonceCoverMapper.findAllFinalStateNull();
		for(EthTradeNonceCover bean : findAllFinalStateNull){
			log.info(bean.toString());
		}
	}
	
	@Test
	public void findSameParam(){
		EthTradeNonceCover findSameParam = ethTradeNonceCoverMapper.findSameParam(
				""
				,"0x763b78bc83ef328a252b25b56ba2a7fe49774b72"
				, "0xb00ecbd39b5138f9eb7680205f565848b3699742"
				, new BigDecimal(0)
				, new BigDecimal(7)
				, "0xa9059cbb0000000000000000000000004fe415ddf8451c9de3f89d8f815f0d014fbe567b0000000000000000000000000000000000000000000000000000000000002710");
		assertTrue(findSameParam != null);
	}
}
