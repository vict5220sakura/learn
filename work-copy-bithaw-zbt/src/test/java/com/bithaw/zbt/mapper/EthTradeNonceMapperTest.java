/**
 * @Description EthTradeNonceMapper 单元测试文件
 * @author  WangWei
 * @Date    2018年8月24日  下午2:22:28
 * @version   V 1.0
 */
package com.bithaw.zbt.mapper;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.entity.EthTradeNonce;

/**
 * @Description EthTradeNonceMapper单元测试类
 * @author   WangWei
 * @date     2018年8月24日 下午2:22:28
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
@Transactional
public class EthTradeNonceMapperTest {
	
	@Autowired
	private EthTradeNonceMapper ethTradeNonceMapper;
	
	/**
	 * @author WangWei
	 * @Description 构造测试数据,测试需要添加回滚
	 * @method before 
	 * @return void
	 * @date 2018年8月24日 下午2:24:17
	 */
	@Before
	public void before(){
		EthTradeNonce ethTradeNonce = new EthTradeNonce.Builder()//
			.setCreateTime(new Date())//
			.setData("TEST_data")//
			.setFromAddress("test_fromAddress")//
			.setGasLimit(0L)//
			.setGasPrice(new BigDecimal("0"))//
			.setNonce(0)//
			.setOrderNo("test_OrderNo")//
			.setRawTransaction("test_rawTracsaction")//
			.setState(0)//
			.setStateErrorCode("test_stateErrorCode")//
			.setStateErrorMessage("test_StateErrorMessage")//
			.setToAddress("test_ToAddress")//
			.setTxhash("test_Txhash")//
			.setValue(new BigDecimal("0"))//
			.build();
		ethTradeNonceMapper.save(ethTradeNonce);
	}
	
	@Before
	public void before4findAllRawTranssactionNullTest(){
		EthTradeNonce ethTradeNonce = new EthTradeNonce.Builder()//
				.setOrderNo("test_OrderNo2")//
				.setFromAddress("test_fromAddress2")//
				.setToAddress("test_ToAddress")//
				.setValue(new BigDecimal("0"))//
				.setData("TEST_data2")//
				.setNonce(1)//
				.setGasLimit(0L)//
				.setGasPrice(new BigDecimal("0"))//
				.setTxhash("test_Txhash2")//
				.setCreateTime(new Date())//
				.setState(0)//
				.setCoverState(1)
				.build();
		ethTradeNonceMapper.save(ethTradeNonce);
	}
	
	/**
	 * @author WangWei
	 * @Description 测试根据order_no获取对象,需要开启eureka,trade
	 * @method findByorderNoTest 
	 * @return void
	 * @date 2018年8月24日 下午2:30:29
	 */
	@Test
	public void findByorderNoTest(){
		EthTradeNonce findByorderNo = ethTradeNonceMapper.findByorderNo("test_OrderNo");
		assertTrue(findByorderNo != null);
		EthTradeNonce findByorderNoNull = ethTradeNonceMapper.findByorderNo("test_OrderNo_null");
		assertTrue(findByorderNoNull == null);
	}
	
	/**
	 * @author WangWei
	 * @Description 测试根据FromAddress获取对象集合,需要开启eureka,trade
	 * @method findAllByFromAddressTest 
	 * @return void
	 * @date 2018年8月24日 下午2:31:46
	 */
	@Test
	public void findAllByFromAddressTest(){
		List<EthTradeNonce> findAllByFromAddress = ethTradeNonceMapper.findAllByFromAddress("test_fromAddress");
		assertTrue(findAllByFromAddress.size() == 1);
		List<EthTradeNonce> findAllByFromAddressNull = ethTradeNonceMapper.findAllByFromAddress("test_fromAddress_null");
		assertTrue(findAllByFromAddressNull.size() == 0);
	}
	
	/**
	 * @author WangWei
	 * @Description 测试查找一个FromAddress的nonce数组,需要开启eureka,trade
	 * @method findAllByFromAddressTest 
	 * @return void
	 * @date 2018年8月24日 下午2:31:46
	 */
	@Test
	public void findNonceByFromAddressTest(){
		List<Integer> findNonceByFromAddress = ethTradeNonceMapper. findNonceByFromAddress("test_fromAddress");
		assertTrue(findNonceByFromAddress.size() == 1);
		List<Integer> findNonceByFromAddressNull = ethTradeNonceMapper. findNonceByFromAddress("test_fromAddress_null");
		assertTrue(findNonceByFromAddressNull.size() == 0);
		List<Integer> findNonceByFromAddressCustom = ethTradeNonceMapper. findNonceByFromAddress("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		System.out.println(findNonceByFromAddressCustom.size());
	}
	
	/**
	 * @author WangWei
	 * @Description 找到所有没有签名的交易数据测试
	 * @method findAllRawTranssactionNullTest 
	 * @return void
	 * @date 2018年8月27日 下午3:26:32
	 */
	@Test
	public void findAllRawTranssactionNullTest(){
		List<EthTradeNonce> findAllRawTranssactionNull = ethTradeNonceMapper.findAllRawTranssactionNull();
		assertTrue(findAllRawTranssactionNull.size() == 1);
	}
	
	@Test 
	public void findFromAddressesListTest(){
		List<String> findFromAddresses = ethTradeNonceMapper.findFromAddressesList();
	}
	
	@Test
	public void findByFromAddressTest(){
		List<EthTradeNonce> findByFromAddress = ethTradeNonceMapper.findAllNonceNull("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		for(EthTradeNonce ethTradeNonce : findByFromAddress){
			System.out.println(ethTradeNonce);
		}
	}
}
