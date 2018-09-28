/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  下午12:00:41
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.BithawBtcApplication;
import com.bithaw.btc.entity.UTXO;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月31日 下午4:00:06
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BitRestUtilsTest {
	@Autowired
	BitRestUtils bitRestUtils;
	
	@Test
	public void gettxoutTest() throws Throwable{
		UTXO utxo = bitRestUtils.gettxout("0fc6df82e8d43dda5518575e5565c506cdbdb5d8def1a6fc55822f1e5503b23b", 0);
		System.out.println(utxo);
	}
	
	/**
	 * @author WangWei
	 * @Description 查看一个地址是否存在与本地节点中 测试
	 * @method isExistTest 
	 * @return void
	 * @date 2018年9月4日 下午4:52:54
	 */
	@Test
	public void isExistTest(){
		boolean exist = bitRestUtils.isExist("1G9dBLRzmimYjLE5xN2hMX5ubCSygwMh7q");
		assertTrue(exist);
	}
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method getUtxosByAddressTest 
	 * @return void
	 * @date 2018年9月4日 下午7:48:12
	 */
	@Test
	public void getUtxosByAddressTest(){
		Optional<List<UTXO>> utxosByAddress = bitRestUtils.getUtxosByAddress("1G9dBLRzmimYjLE5xN2hMX5ubCSygwMh7q");
		assertTrue(utxosByAddress.get().size()>0);
		for(UTXO utxo : utxosByAddress.orElse(new ArrayList<UTXO>())){
			log.info(utxo.toString());
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method getHeight 
	 * @return void
	 * @date 2018年9月4日 下午7:48:15
	 */
	@Test
	public void getHeight(){
		Optional<Integer> height = bitRestUtils.getHeight();
		assertTrue(height.isPresent());
		log.info(height.get() + "");
	}
	
	
	/**
	 * @author WangWei
	 * @Description 返回交易确认数,需要手动更改确认数
	 * @method getConfirmationsBtTxHashTest 
	 * @return void
	 * @date 2018年9月5日 上午10:17:55
	 */
	@Test
	public void getConfirmationsBtTxHashTest(){
		Optional<Long> confirmationsBtTxHash = bitRestUtils.getConfirmationsBtTxHash("f84d43b4203e9af267de1775aba638aa53ca9fc3cca82dece1d935bb2d08e65f");
		assertTrue(confirmationsBtTxHash.orElseGet(() -> new Long(0)).equals(Long.valueOf("0")));
		Optional<Long> confirmationsBtTxHash2 = bitRestUtils.getConfirmationsBtTxHash("e84d43b4203e9af267de1775aba638aa53ca9fc3cca82dece1d935bb2d08e65f");
		log.info(confirmationsBtTxHash2.get() + "");
	}
	
	@Test
	public void getConnectionCount(){
		Optional<Integer> connectionCount = bitRestUtils.getConnectionCount();
		log.info(connectionCount.toString());
	}
	
	@Test
	public void gettxout() throws Exception{
		UTXO gettxout = bitRestUtils.gettxout("44773d2be263999c816691479f32da9e30ac167d46ce1cd5de0d33cf09103292", 0);
		log.info(gettxout.toString());
	}
}
