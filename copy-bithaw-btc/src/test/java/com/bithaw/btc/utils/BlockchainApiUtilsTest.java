/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月4日  下午8:19:22
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

import com.bithaw.btc.BithawBtcApplication;
import com.bithaw.btc.entity.UTXO;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月4日 下午8:19:22
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BlockchainApiUtilsTest {
	@Autowired
	BlockchainApiUtils blockchainApiUtils;
	
	@Test
	public void getUTXOsTest(){
		Optional<List<UTXO>> utxOs = blockchainApiUtils.getUTXOs("1G9dBLRzmimYjLE5xN2hMX5ubCSygwMh7q");
		assertTrue(utxOs.get().size() > 0);
		log.info(utxOs.get().size() + "");
		for(UTXO utxo : utxOs.orElse(new ArrayList<UTXO>())){
			log.info(utxo.toString());
		}
	}
	
	@Test
	public void getBlockHeightTest(){
		Optional<Long> blockHeight = blockchainApiUtils.getBlockHeight();
		log.info(blockHeight.get() + "");
	}
	
	@Test
	public void getConfirmationsBtTxHashTest(){
		Optional<Long> confirmationsBtTxHash = blockchainApiUtils.getConfirmationsBtTxHash("e84d43b4203e9af267de1775aba638aa53ca9fc3cca82dece1d935bb2d08e65f");
		log.info(confirmationsBtTxHash.get() + "");
	}
}
