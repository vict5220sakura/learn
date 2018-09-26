/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  上午10:36:22
 * @version   V 1.0
 */
package com.bithaw.zbt.solidity.bhaw.v1;

import static org.junit.Assert.assertTrue;

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
 * @date     2018年9月11日 上午10:36:22
 * @version  V 1.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawZBTTradeApplication.class)
public class BhawV1SelectByWeb3jTest {
	@Autowired
	private BhawV1SelectByWeb3j bhawV1SelectByWeb3j;
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method balanceOfTest 
	 * @return void
	 * @date 2018年9月11日 上午10:37:30
	 */
	@Test
	public void balanceOfTest(){
		BigInteger bigInteger = bhawV1SelectByWeb3j.balanceOf("0x763b78bc83ef328a252b25b56ba2a7fe49774b72","0xb00EcbD39B5138F9EB7680205F565848B3699742");
		assertTrue(bigInteger != null);
		String string = bigInteger.toString();
		log.info( new BigDecimal(string)
				.divide( new BigDecimal("1000000"), 6, BigDecimal.ROUND_HALF_UP )  
				.toPlainString());
	}
	@Test
	public void ownerTest(){
		String owner = bhawV1SelectByWeb3j.owner("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(owner != null);
		log.info(owner);
	}
	@Test
	public void totalSupplyTest(){
		BigInteger totalSupply = bhawV1SelectByWeb3j.totalSupply("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(totalSupply != null);
		log.info(totalSupply.toString());//5000000000.000000
	}
	@Test
	public void allowanceTest(){
		BigInteger allowance = bhawV1SelectByWeb3j.allowance("0x763b78bc83ef328a252b25b56ba2a7fe49774b72","0x00b8eb7d9eb0d47924e68b56a2f6599ef844b617","0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(allowance != null);
		log.info(allowance.toString());//5000000000.000000
	}
	@Test
	public void freezeFlagTest(){
		Optional<Boolean> freezeFlag = bhawV1SelectByWeb3j.freezeFlag("0x763b78bc83ef328a252b25b56ba2a7fe49774b72", "0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(freezeFlag.isPresent());
		log.info(freezeFlag.toString());
	}
	@Test
	public void nameTest(){
		Optional<String> name = bhawV1SelectByWeb3j.name("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(name.isPresent());
		log.info(name.toString());
	}
	@Test
	public void symbolTest(){
		Optional<String> symbol = bhawV1SelectByWeb3j.symbol("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(symbol.isPresent());
		log.info(symbol.toString());
	}
	@Test
	public void decimalsTest() throws Exception{
		Optional<BigInteger> decimals = bhawV1SelectByWeb3j.decimals("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(decimals.isPresent());
		assertTrue(decimals.orElseThrow(()->new Exception()).toString().equals("6"));
		log.info(decimals.toString());
	}
	@Test
	public void sellPriceTest() throws Exception{
		Optional<BigInteger> sellPrice = bhawV1SelectByWeb3j.sellPrice("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(sellPrice.isPresent());
		log.info(sellPrice.toString());
	}
	@Test
	public void buyPriceTest() throws Exception{
		Optional<BigInteger> buyPrice = bhawV1SelectByWeb3j.buyPrice("0x763b78bc83ef328a252b25b56ba2a7fe49774b72");
		assertTrue(buyPrice.isPresent());
		log.info(buyPrice.toString());
	}
}
