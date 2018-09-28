/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午3:49:51
 * @version   V 1.0
 */
package com.bithaw.btc.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;
import com.bithaw.btc.entity.BtcTradeInput;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午3:49:51
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BtcTradeInputMapperTest {
	@Autowired
	BtcTradeInputMapper btcTradeInputMapper;
	
	@Test
	public void insertTest(){
		BtcTradeInput btcTradeInput = new BtcTradeInput().builder()
			.uuid("测试")
			.address("测试")
			.amount(new BigDecimal("1.1"))
			.confirmations(255)
			.createTime(new Date())
			.txIndex(0)
			.scriptPubkey("测试")
			.txId("测试")
			.build();
		btcTradeInputMapper.save(btcTradeInput);
	}
	
	@Test
	public void findByTxIdAndTxIndexTest(){
		BtcTradeInput findByTxIdAndTxIndex = btcTradeInputMapper.findByTxIdAndTxIndex("测试", 1);
		System.out.println(findByTxIdAndTxIndex);
	}
	
	@Test
	public void findByUuidTest(){
		List<BtcTradeInput> findByUuid = btcTradeInputMapper.findByUuid("5220");
		for(BtcTradeInput btcTradeInput : findByUuid){
			System.out.println(btcTradeInput);
		}
	}
}
