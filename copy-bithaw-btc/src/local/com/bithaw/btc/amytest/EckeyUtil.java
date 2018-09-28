/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月30日  下午1:46:18
 * @version   V 1.0
 */
package com.bithaw.btc.amytest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.spec.ECPoint;
import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.core.Message;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Transaction.SigHash;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.Script.ScriptType;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptChunk;
import org.junit.Test;
import org.springframework.security.crypto.codec.Hex;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月30日 下午1:46:18
 * @version  V 1.0
 */
public class EckeyUtil {
	@Test
	public void hexKeyConver(){
		TestNet3Params testNet3Params = TestNet3Params.get();
		MainNetParams mainNetParams = MainNetParams.get();
		ECKey key = ECKey.fromPrivate(Hex.decode("64a2c1971f6a3617062c6f5d72a26627e514e349b2e0d2429a822a7733359bf0"));
		System.out.println("16进制公钥:" + key.getPublicKeyAsHex());
		System.out.println("16进制私钥:" + key.getPrivateKeyAsHex());
		System.out.println("测试地址公钥:" + key.toAddress(testNet3Params).toBase58());
		System.out.println("测试地址私钥:" + key.getPrivateKeyAsWiF(testNet3Params));
		System.out.println("正式地址公钥:" + key.toAddress(mainNetParams).toBase58());
		System.out.println("正式地址私钥:" + key.getPrivateKeyAsWiF(mainNetParams));
	}
	
	@Test
	public void PrivateKeyBase58(){
		TestNet3Params testNet3Params = TestNet3Params.get();
		MainNetParams mainNetParams = MainNetParams.get();
		DumpedPrivateKey dpk = DumpedPrivateKey.fromBase58(mainNetParams, "L1eQCKerqoq4cV7eg6pE2cpVdDuWTVfCRrnrqe9GSnQvzAJkqkYq");
		System.out.println("16进制公钥:" + dpk.getKey().getPublicKeyAsHex());
		System.out.println("16进制私钥:" + dpk.getKey().getPrivateKeyAsHex());
		System.out.println("测试地址公钥:" + dpk.getKey().toAddress(testNet3Params).toBase58());
		System.out.println("测试地址私钥:" + dpk.getKey().getPrivateKeyAsWiF(testNet3Params));
		System.out.println("正式地址公钥:" + dpk.getKey().toAddress(mainNetParams).toBase58());
		System.out.println("正式地址私钥:" + dpk.getKey().getPrivateKeyAsWiF(mainNetParams));
	}
	
	@Test
	public void newAddress(){
		TestNet3Params testNet3Params = TestNet3Params.get();
		MainNetParams mainNetParams = MainNetParams.get();
		
		ECKey ceKey = new ECKey();
		String privateKeyWiF = ceKey.getPrivateKeyAsWiF(mainNetParams);
		
		DumpedPrivateKey dpk = DumpedPrivateKey.fromBase58(mainNetParams, privateKeyWiF);
		System.out.println("16进制公钥:" + dpk.getKey().getPublicKeyAsHex());
		System.out.println("16进制私钥:" + dpk.getKey().getPrivateKeyAsHex());
		System.out.println("测试地址公钥:" + dpk.getKey().toAddress(testNet3Params).toBase58());
		System.out.println("测试地址私钥:" + dpk.getKey().getPrivateKeyAsWiF(testNet3Params));
		System.out.println("正式地址公钥:" + dpk.getKey().toAddress(mainNetParams).toBase58());
		System.out.println("正式地址私钥:" + dpk.getKey().getPrivateKeyAsWiF(mainNetParams));
	}
	
	@Test
	public void test1(){
		MainNetParams mainNetParams = MainNetParams.get();
		
		Transaction transaction = new Transaction(mainNetParams);
		transaction.setVersion(2);
		
		transaction.addOutput(//
				Coin.valueOf( new BigDecimal("0.00142000").multiply(new BigDecimal("100000000")).longValue() )//
				, Address.fromBase58(mainNetParams, "1G9dBLRzmimYjLE5xN2hMX5ubCSygwMh7q")//
				);
		
//		transaction.addOutput(//
//				Coin.valueOf( new BigDecimal("0.00001").multiply(new BigDecimal("100000000")).longValue() )//
//				, Address.fromBase58(mainNetParams, "1278PmjsztTdkfq943KTSd7nfVXy5ptQ89")//
//				);
		
		Transaction transactionIn2 = new Transaction(mainNetParams, Hex.decode("0200000004cdc3feccc07fd9c3dad65814b393ec7c264cc73e47eac9cc32a422e537a8f7d7000000006b483045022100e90345e9ad26f0b7f3cacb4d1b5b5a5e8d99b6c5b1d40b2348622b2577dc298a022035dde0c889f4229285fc22e6b0114648924790788ff103251630dee6735b3986832103d528dcdee5b2d4638248ac50ced8050230626a45bccf82b3198ed6f8f6769106ffffffff5c69fdd878d6a6521c8be83f8aad97ca420b8501339b4abdd3e9f37883ec2c8c000000006b4830450221009815ecc0a1e767deec40a03e66e9cb54d62a2c1674318aac40dd0698a85fc93502206d4d2a4e29458f5b279310db8cd9cf38283d6ef6b7a4e298a26c77f6fca479f3832103d528dcdee5b2d4638248ac50ced8050230626a45bccf82b3198ed6f8f6769106ffffffff5c69fdd878d6a6521c8be83f8aad97ca420b8501339b4abdd3e9f37883ec2c8c010000006a473044022063e95f499e55282cafe9abb2b1978b68ce40090b12bed7a8d7f7d6a1d1b6ebc202203cebb6fa69a5a8709939003234ef978d4fa9182b72cbfe3c8d787a02704a6d2783210356a7322f4f2a8b6cb01b6d8439c4641065926c86e7c3832b0a542458043eeed7ffffffffcdc3feccc07fd9c3dad65814b393ec7c264cc73e47eac9cc32a422e537a8f7d7010000006a473044022021a93c10a71266202a6a848252b83668a25ebaa46ac3d3ca86dc36d00decc8fb02203773f4bda9f2465db71e23173c592666836ff6bed61733a92a696c10a3def2428321030a995372be7f11d45987fba6c9d710f71b790b2f0914eda45ab58d777fafcbeeffffffff01982e0200000000001976a914a62b6a1b9026ff013effae230d7d12ff9d4f62fd88ac00000000"));
		DumpedPrivateKey dpk2 = DumpedPrivateKey.fromBase58(mainNetParams, "KynFVz4K14wLQNBAJAZG48XEzPTPN2hGXEPoqLqJnXPzN8YGHgef");
		transactionIn2.getOutput(0).setValue(Coin.valueOf( new BigDecimal("0.00143000").multiply(new BigDecimal("100000000")).longValue() ));
		transaction.addSignedInput(transactionIn2.getOutput(0), dpk2.getKey(),SigHash.ALL,false);
		
		transaction.getInput(0).setSequenceNumber(System.currentTimeMillis());
		
//		Transaction transactionIn = new Transaction(mainNetParams, HexBin.decode("0200000000010173c9bd8356a092e364405aa94188980ebd3d31bbca11ad44bd02642d53756af2000000001716001487c6e2a4d18b9807184497f5dc6f3b1369cdf5f6feffffff02e8030000000000001976a914fcd5d0154469b0372f0789d393725d03167c804088ac621700000000000017a914a6f1b28b343d6eb7b8218f2f2f649296ffee91c8870247304402203aa5b0468cf292170eeec07e924544e730795053a421d7f9608e8b66c5a0d899022072617fba534246497a89df99615a35c92388bfa6d9c4a091a06e6a1d0443efba0121031fc72e178ebe3ba25d8b2afe8ad5d43ca27d3f363e3a1bf4ef14a48c18dcedbce8380800"));
//		DumpedPrivateKey dpk = DumpedPrivateKey.fromBase58(mainNetParams, "KzZsfMDWefWF52YBocbHZgNWg4b3seqgweGBcmSHwCW23T1qfYj7");
//		transactionIn.getOutput(0).setValue(Coin.valueOf( new BigDecimal("0.00001").multiply(new BigDecimal("100000000")).longValue() ));
//		transaction.addSignedInput(transactionIn.getOutput(0), dpk.getKey(),SigHash.SINGLE,true);
		
		byte[] bitcoinSerialize = transaction.bitcoinSerialize();
	}
}
