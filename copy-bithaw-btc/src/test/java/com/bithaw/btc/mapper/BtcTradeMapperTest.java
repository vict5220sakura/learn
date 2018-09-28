/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午2:41:08
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
import com.bithaw.btc.entity.BtcTrade;

import lombok.extern.slf4j.Slf4j;


/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午2:41:08
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
@Slf4j
public class BtcTradeMapperTest {
	@Autowired
	BtcTradeMapper BtcTradeMapper;
	
	@Test
	public void insertTest(){
		BtcTrade btcTrade = new BtcTrade().builder()
			.uuid("5227")
			.amount(new BigDecimal("0.0001"))
			.createTime(new Date())
			.fees(new BigDecimal("0.00001"))
			.state(0)
			.toAddress("1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F")
			.build();
		BtcTradeMapper.save(btcTrade);
	}
	
	@Test
	public void findByStateTest(){
		List<BtcTrade> findByState = BtcTradeMapper.findByState(0);
		for(BtcTrade b : findByState){
			System.out.println(b);
		}
	}
	
	@Test
	public void findByUuidTest(){
		BtcTrade findByUuid = BtcTradeMapper.findByUuid("测试");
		System.out.println(findByUuid);
	}
}
