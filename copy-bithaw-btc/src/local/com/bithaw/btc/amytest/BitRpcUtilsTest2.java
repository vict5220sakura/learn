package com.bithaw.btc.amytest;

import java.util.Map;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.btc.BithawBtcApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BithawBtcApplication.class)
public class BitRpcUtilsTest2 {
	
	
	private static String ip;// = "47.52.190.231";//链接地址
	
//	@Value("${btc.rpc.ip}")
//	public void setIp(String ip) {
//		BitRpcUtilsTest2.ip = ip;
//	}

//	@Test
//	public void testIp(){
//		System.out.println("测试 : " + ip);
//	}
	
//	public static void main(String[] args) throws Throwable {
//		decoderawtransaction();
//	}
	
//	public static void newAddress(){//ok
//		String address = BitRpcUtils.getInstance().newAddress();
//		System.out.println(address);
//	}
//	public static void getHeight() throws Throwable{//ok
//		Integer height = BitRpcUtils.getInstance().getBlockHeight();
//		System.out.println(height);
//	}
//	public static void unlock() throws Throwable{
//		System.out.println(BitRpcUtils.getInstance().walletpassphase("5462", 60));
//	}
//	public static void trade() throws Throwable{
//		String txHash = BitRpcUtils.getInstance().trade("mhVKwB7HJFzNAC6wDvRGSunMamXg53r6Wk", 0.1);
//		System.out.println(txHash);//93a7f2d34880f049f21430d7d522599e301543e4251f695472caa4b0885a1002
//	}
//	public static void getBalance() throws Throwable{
//		double balance = BitRpcUtils.getInstance().getBalance();
//		System.out.println(balance);//2.74995076
//	}
//	public static void getrawtransaction() throws Throwable{
//		String getrawtransaction = BitRpcUtils.getInstance().getrawtransaction("93a7f2d34880f049f21430d7d522599e301543e4251f695472caa4b0885a1002");
//		System.out.println(getrawtransaction);//020000000001017f447fe1f8f831da57e20a79486e05165319ab417bc4ceed1c11538074dbae770100000017160014774e2ae9d6cf3ffbc0ed84f49f2d487a3c6ef17cfeffffff0246fda9020000000017a914237369f54e80d35969dcea897737e0977aaa0a288780969800000000001976a91415a17bdbbe45a9b6edc5c492f4e3ee8c2927bee188ac0247304402207e400f91e8ff48dc46940fd376c24736c4e693041b9340f65e61b3323332a2460220404c70cebc87edacf332af7ad27be86c2e083bcf0a58b2008dccb50ac8f04e9a0121033e903d5e36affca59d28c1d7ccdf7c466904c6c144896dc12ae4a77f77abef546cae1400
//	}
//	public static void decoderawtransaction() throws Throwable{
//		JSONObject decoderawtransaction = BitRpcUtils.getInstance().decoderawtransaction("020000000001017f447fe1f8f831da57e20a79486e05165319ab417bc4ceed1c11538074dbae770100000017160014774e2ae9d6cf3ffbc0ed84f49f2d487a3c6ef17cfeffffff0246fda9020000000017a914237369f54e80d35969dcea897737e0977aaa0a288780969800000000001976a91415a17bdbbe45a9b6edc5c492f4e3ee8c2927bee188ac0247304402207e400f91e8ff48dc46940fd376c24736c4e693041b9340f65e61b3323332a2460220404c70cebc87edacf332af7ad27be86c2e083bcf0a58b2008dccb50ac8f04e9a0121033e903d5e36affca59d28c1d7ccdf7c466904c6c144896dc12ae4a77f77abef546cae1400");
//		System.out.println(decoderawtransaction.get("locktime"));
//	}
//	
//	@Test
//	public void decoderawtransaction1() throws Throwable{
//		JSONObject decoderawtransaction = BitRpcUtils.getInstance().decoderawtransaction("020000000001017f447fe1f8f831da57e20a79486e05165319ab417bc4ceed1c11538074dbae770100000017160014774e2ae9d6cf3ffbc0ed84f49f2d487a3c6ef17cfeffffff0246fda9020000000017a914237369f54e80d35969dcea897737e0977aaa0a288780969800000000001976a91415a17bdbbe45a9b6edc5c492f4e3ee8c2927bee188ac0247304402207e400f91e8ff48dc46940fd376c24736c4e693041b9340f65e61b3323332a2460220404c70cebc87edacf332af7ad27be86c2e083bcf0a58b2008dccb50ac8f04e9a0121033e903d5e36affca59d28c1d7ccdf7c466904c6c144896dc12ae4a77f77abef546cae1400");
//		System.out.println(decoderawtransaction.get("locktime"));
//	}
}
