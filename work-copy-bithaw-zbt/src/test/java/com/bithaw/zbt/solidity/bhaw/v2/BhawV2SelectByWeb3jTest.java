/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  上午10:36:22
 * @version   V 1.0
 */
package com.bithaw.zbt.solidity.bhaw.v2;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.zbt.BithawZBTTradeApplication;
import com.bithaw.zbt.solidity.bhaw.v2.BhawV2SelectByWeb3j;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月11日 上午10:36:22
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class BhawV2SelectByWeb3jTest {
	@Autowired
	private BhawV2SelectByWeb3j bhawV2SelectByWeb3j;
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method balanceOfTest 
	 * @return void
	 * @date 2018年9月11日 上午10:37:30
	 */
	@Test
	public void balanceOfTest(){
		BigInteger bigInteger = bhawV2SelectByWeb3j.balanceOf("0x763b78bc83ef328a252b25b56ba2a7fe49774b72","0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(bigInteger != null);
		log.info(bigInteger.toString());
	}
	@Test
	public void ownerTest(){
		String owner = bhawV2SelectByWeb3j.owner("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(owner != null);
		log.info(owner);
	}
	@Test
	public void totalSupplyTest(){
		BigInteger totalSupply = bhawV2SelectByWeb3j.totalSupply("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(totalSupply != null);
		log.info(totalSupply.toString());//5000000000.000000
	}
	@Test
	public void allowanceTest(){
		BigInteger allowance = bhawV2SelectByWeb3j.allowance("0x763b78bc83ef328a252b25b56ba2a7fe49774b72","0x00b8eb7d9eb0d47924e68b56a2f6599ef844b617","0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(allowance != null);
		log.info(allowance.toString());//5000000000.000000
	}
	@Test
	public void freezeFlagTest(){
		Optional<Boolean> freezeFlag = bhawV2SelectByWeb3j.freezeFlag("0x763b78bc83ef328a252b25b56ba2a7fe49774b72", "0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(freezeFlag.isPresent());
		log.info(freezeFlag.toString());
	}
	@Test
	public void nameTest(){
		Optional<String> name = bhawV2SelectByWeb3j.name("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(name.isPresent());
		log.info(name.toString());
	}
	@Test
	public void symbolTest(){
		Optional<String> symbol = bhawV2SelectByWeb3j.symbol("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(symbol.isPresent());
		log.info(symbol.toString());
	}
	@Test
	public void decimalsTest() throws Exception{
		Optional<BigInteger> decimals = bhawV2SelectByWeb3j.decimals("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(decimals.isPresent());
		assertTrue(decimals.orElseThrow(()->new Exception()).toString().equals("6"));
		log.info(decimals.toString());
	}
}
