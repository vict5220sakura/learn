/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午4:24:40
 * @version   V 1.0
 */
package com.bithaw.btc.mapper;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;
import com.bithaw.btc.entity.BtcTradeOutput;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午4:24:40
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BtcTradeOutputMapperTest {
	@Autowired
	BtcTradeOutputMapper btcTradeOutputMapper;
	
	@Test
	public void insertTest(){
		BtcTradeOutput build = new BtcTradeOutput().builder()
			.address("测试")
			.amount(new BigDecimal("1.1"))
			.createTime(new Date())
			.uuid("测试")
			.build();
		btcTradeOutputMapper.save(build);
	}
}
