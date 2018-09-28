/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  下午6:01:49
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;
import com.bithaw.btc.exception.OutlineSignAmountException;
import com.bithaw.btc.utils.BtcOutlineUtils.Input;
import com.bithaw.btc.utils.BtcOutlineUtils.Output;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月31日 下午6:01:49
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BtcOutlineUtilsTest {
	@Autowired
	private BtcOutlineUtils bTCOutlineUtils;
	
	@Test
	public void outLineSignTest() throws OutlineSignAmountException{
		ArrayList<Input> inputs = new ArrayList<Input>();
		inputs.add(new Input.Builder()//
				.setAddress("1G9dBLRzmimYjLE5xN2hMX5ubCSygwMh7q")
				.setInputTxid("cd49a0184603e3765bd4d0fb431263ff203469d3bf8c0a87b12d065f016ac8f6")//
				.setInputIndex(0L)//
				.setAmount("0.00088000")//
				.setScriptPubKey("76a914a62b6a1b9026ff013effae230d7d12ff9d4f62fd88ac")//
				.setPrivateKeyHex("4c6a20fca39654104f099135038e70ced2cd961c9c59a4e0560e09fdd60e8f83")//
				.build());
		
		List<Output> outs = new ArrayList<Output>();
		outs.add(new Output.Builder()//
				.setAddress("1G9dBLRzmimYjLE5xN2hMX5ubCSygwMh7q")//
				.setAmount("0.00087000")//
				.build());
//		outs.add(new Output.Builder()//
//				.setAddress("17CYmBBe6VHPAfxxTV7Tt3zMZwW3SQSnKz")//
//				.setAmount("0.0001")//
//				.build());
		
		String outLineSign = bTCOutlineUtils.outLineSign( inputs, outs, "0.00001");
		System.out.println(outLineSign);
	}
}
