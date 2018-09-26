/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  上午11:16:34
 * @version   V 1.0
 */
package com.bithaw.zbt.solidity.bhaw.v1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.zbt.BithawZBTTradeApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月12日 上午11:16:34
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class BhawV1SelectByEtherscanTest {
	@Autowired
	BhawV1SelectByEtherscan bhawV1SelectByEtherscan;
	
	@Test
	public void balanceOf(){
		BigDecimal balanceOf = bhawV1SelectByEtherscan.balanceOf("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		log.info(balanceOf.toPlainString());
	}
	@Test
	public void owner(){
		String owner = bhawV1SelectByEtherscan.owner("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		log.info(owner);
	}
	@Test
	public void totalSupply(){
		BigDecimal totalSupply = bhawV1SelectByEtherscan.totalSupply();
		log.info(totalSupply.toPlainString());
	}
	@Test
	public void allowance(){
		BigDecimal allowance = bhawV1SelectByEtherscan.allowance("0x00b8eb7d9eb0d47924e68b56a2f6599ef844b617","0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		log.info(allowance.toPlainString());
	}
	@Test
	public void freezeFlag(){
		Optional<Boolean> freezeFlag = bhawV1SelectByEtherscan.freezeFlag("0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b");
		log.info(freezeFlag.toString());
	}
	@Test
	public void name(){
		Optional<String> name = bhawV1SelectByEtherscan.name();
		log.info(name.toString());
	}
	@Test
	public void symbol(){
		Optional<String> symbol = bhawV1SelectByEtherscan.symbol();
		log.info(symbol.toString());
	}
	@Test
	public void decimals(){
		Optional<BigInteger> decimals = bhawV1SelectByEtherscan.decimals();
		log.info(decimals.toString());
	}
}
