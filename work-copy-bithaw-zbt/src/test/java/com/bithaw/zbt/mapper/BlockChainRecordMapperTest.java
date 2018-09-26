/**
 * @Description BlockChainRecordMapper junit测试文件
 * @author  WangWei
 * @Date    2018年8月24日  下午1:51:11
 * @version   V 1.0
 */
package com.bithaw.zbt.mapper;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.entity.BlockChainRecord;


/**
 * @Description BlockChainRecordMapper测试类
 * @author   WangWei
 * @date     2018年8月24日 下午1:51:11
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
@Transactional
public class BlockChainRecordMapperTest {
	
	@Autowired
	private BlockChainRecordMapper blockChainRecordMapper;
	
	/**
	 * @author WangWei
	 * @Description 数据库增加一条测试数据,需要junit回滚
	 * @method before 
	 * @return void
	 * @date 2018年8月24日 下午2:04:38
	 */
	@Before
	public void before() {
		BlockChainRecord build = new BlockChainRecord.Builder()//
				.setActualServiceFee(new BigDecimal("0"))//
				.setAddTime(new BigInteger("0"))//
				.setAmount(new BigDecimal("0"))//
				.setBlockHeight(new BigInteger("15023"))//
				.setCoinType("TEST")//
				.setContent("TEST")//
				.setContractAmount(new BigDecimal("0"))//
				.setContractToAddress("testToAddress")//
				.setFromAddress("testFromAddress")//
				.setStatus(0)//
				.setToAddress("testToAddress")//
				.setTxHash("testTxHash")//
				.build();
		blockChainRecordMapper.save(build);
	}
	
	/**
	 * @author WangWei
	 * @Description 测试根据txhash获取实体,需要开启eureka及trade
	 * @method findByTxHashTest 
	 * @return void
	 * @date 2018年8月24日 下午1:54:21
	 */
	@Test
	public void findByTxHashTest(){
		BlockChainRecord findByTxHash = blockChainRecordMapper.findByTxHash("testTxHash");
		assertTrue(findByTxHash != null);
		BlockChainRecord findByTxHashNull = blockChainRecordMapper.findByTxHash("testTxHash_null");
		assertTrue(findByTxHashNull == null);
	}
	
	/**
	 * @author WangWei
	 * @Description 获取toAddress的最高区块数据,需要开启eureka及trade
	 * @method getMaxBlockNumberByToAddressTest 
	 * @return void
	 * @date 2018年8月24日 下午2:10:50
	 */
	@Test
	public void getMaxBlockNumberByToAddressTest(){
		BigInteger maxBlockNumberByToAddress = blockChainRecordMapper.getMaxBlockNumberByToAddress("testToAddress");
		assertTrue(new BigInteger("15023").equals(maxBlockNumberByToAddress));
		BigInteger maxBlockNumberByToAddressNull = blockChainRecordMapper.getMaxBlockNumberByToAddress("testToAddressNull");
		assertTrue(maxBlockNumberByToAddressNull == null);
	}
}
