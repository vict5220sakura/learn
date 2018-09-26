/**
 * @Description BlockChainTradeRecordMapperTest测试文件
 * @author  WangWei
 * @Date    2018年8月24日  下午2:13:05
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
import com.bithaw.zbt.entity.BlockChainTradeRecord;

/**
 * @Description BlockChainTradeRecordMapper测试类
 * @author   WangWei
 * @date     2018年8月24日 下午2:13:05
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
@Transactional
public class BlockChainTradeRecordMapperTest {
	@Autowired
	private BlockChainTradeRecordMapper blockChainTradeRecordMapper;
	/**
	 * @author WangWei
	 * @Description 测试构造数据,需要测试添加回滚
	 * @method before 
	 * @return void
	 * @date 2018年8月24日 下午2:20:41
	 */
	@Before
	public void before(){
		BlockChainTradeRecord blockChainTradeRecord = new BlockChainTradeRecord.Builder()//
			.setActualServiceFee(new BigDecimal("1"))//
			.setAddTime(new BigInteger("1"))//
			.setAmount(new BigDecimal("1"))//
			.setBlockHeight(new BigInteger("1"))//
			.setCoinType("TEST")//
			.setFromAddress("testFromAddress")//
			.setMerchantId(new BigInteger("1"))//
			.setNotifyStatus(new Integer("1"))//
			.setNotifyUrl("test_NotifyUrl")//
			.setSolidityAddress("test_solidityaddress")//
			.setStatus(new Integer("1"))//
			.setToAddress("test_toAddress")//
			.setTxHash("test_txHash")//
			.setUserId(new BigInteger("1"))//
			.build();
		blockChainTradeRecordMapper.save(blockChainTradeRecord);
	}
	
	/**
	 * @author WangWei
	 * @Description 测试根据txHash查找数据,需要开启eureka,trade
	 * @method findByTxHash 
	 * @return void
	 * @date 2018年8月24日 下午2:20:37
	 */
	@Test
	public void findByTxHash(){
		BlockChainTradeRecord findByTxHash = blockChainTradeRecordMapper.findByTxHash("test_txHash");
		assertTrue(findByTxHash != null);
		BlockChainTradeRecord findByTxHashNull = blockChainTradeRecordMapper.findByTxHash("test_txHash_null");
		assertTrue(findByTxHashNull == null);
	}
}
