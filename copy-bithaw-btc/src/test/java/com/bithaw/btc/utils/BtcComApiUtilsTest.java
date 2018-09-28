/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月7日  下午2:00:45
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;
import com.bithaw.btc.entity.UTXO;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月7日 下午2:00:45
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BtcComApiUtilsTest {
	@Autowired
	BtcComApiUtils btcComApiUtils;
	
	@Test
	public void getConfirmationsBtTxHashTest() throws InterruptedException{
		Optional<Long> confirmationsBtTxHash = btcComApiUtils.getConfirmationsBtTxHash("e84d43b4203e9af267de1775aba638aa53ca9fc3cca82dece1d935bb2d08e65f");
		assertTrue(confirmationsBtTxHash.get() > 0L);
		log.info(confirmationsBtTxHash.toString());
		Optional<Long> confirmationsBtTxHashNull = btcComApiUtils.getConfirmationsBtTxHash("f84d43b4203e9af267de1775aba638aa53ca9fc3cca82dece1d935bb2d08e65f");
		assertFalse(confirmationsBtTxHashNull.isPresent());
	}
	
	/**
	 * @author WangWei
	 * @Description 需要修改验证参数
	 * @method getUTXOsTest 
	 * @return void
	 * @date 2018年9月7日 下午3:23:09
	 */
	@Test
	public void getUTXOsTest(){
		Optional<List<UTXO>> utxOs = btcComApiUtils.getUTXOs("1G9dBLRzmimYjLE5xN2hMX5ubCSygwMh7q");
		assertTrue(utxOs.get().size() == 1);
		log.info(utxOs.toString());
	}
}
