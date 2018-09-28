package com.bithaw.btc.amytest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 比特币工具类
 * @author 王玮
 *
 */
@Slf4j
@Component
public class BitRpcUtils2 {
	@Value("${btc.rpc.ip}")
	private static String ip = "47.52.190.231";//链接地址
	private static String port = "8332";//链接地址
	private static String credUsername = "xcnest";//Authorization身份认证用户名
	private static String credPassword = "6543";//Authorization身份认证用户名
	private static String NetworkParameters = "TestNet3";//网络标识 "MainNet" "TestNet3"

	public static void main(String[] args) {
		String cred = Base64.encodeBase64String((credUsername + ":" + credPassword).getBytes());
		System.out.println(cred);
	}

	private BitRpcUtils2(){
		try {
			init();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.error("初始化bitRpc客户端错误",e);
		}
	}
	
	private void init() throws MalformedURLException {
		String cred = Base64.encodeBase64String((credUsername + ":" + credPassword).getBytes());
		Map<String, String> headers = new HashMap<String, String>(1);
		headers.put("Authorization", "Basic " + cred);
		client = new JsonRpcHttpClient(new URL("http://" + ip + ":" + port), headers);
		
		if("MainNet".equals(NetworkParameters)){
			params = MainNetParams.get();
		}else{
			params = TestNet3Params.get();
		}
	}

	@Component
	private static class BitRpcUtilsBuild{
		@Value("${btc.rpc.ip}")
		private static String ip2;//链接地址
		private static final BitRpcUtils2 INSTANCE  = new BitRpcUtils2();
		static{
			ip = ip2;
		}
	}
	
	public static final BitRpcUtils2 getInstance(){
		return BitRpcUtilsBuild.INSTANCE;
	}
	
	private JsonRpcHttpClient client;
	private NetworkParameters params;
	
	/**
	 * 新建账户
	 * @return 私钥|公钥 
	 * @return "|"分割
	 */
	public String newAddress(){
		ECKey ceKey = new ECKey();
		String privateKeyWiF = ceKey.getPrivateKeyAsWiF(params); // 私钥， WIF(Wallet Import Format)
		log.info("privateKeyWiF : " + privateKeyWiF);
		Address address = ceKey.toAddress(params);//公钥 base58
		log.info("Public Address : " + address.toString());
		return privateKeyWiF + "|" + address.toString();
	}
	
	/**
	 * 公钥地址合法性校验
	 */
	public boolean checkAddress(String address){
		try{ 
			Address.fromBase58(params, address);
		}catch(Exception e){
			log.error("账号合法性校验错误",e);
			return false;
		}
		return true;
	}
	/**
	 * 获取区块高度
	 */
	public Integer getBlockHeight() throws Throwable{
		return (Integer)client.invoke("getblockcount", new Object[] {}, Integer.class);
	}
	
	/**
	 * 解锁bit钱包
	 */
	public boolean walletpassphase(String password,int time){
		try {
			client.invoke("walletpassphrase", new Object[] {password,time});
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	/**
	 * 转账
	 * @return txhash
	 * @throws Throwable 
	 */
	public String trade(String toAddress,double amount) throws Throwable{
		return (String)client.invoke("sendtoaddress", new Object[] {toAddress,amount},Object.class);
	}
	/**
	 * 获取钱包余额
	 * @return
	 */
	public double getBalance() throws Throwable{
		return (double)client.invoke("getbalance", new Object[] {},Object.class);
	}
	/**
	 * 根据交易hash获取交易16进制字符串
	 */
	public String getrawtransaction(String txHash) throws Throwable{
		return (String)client.invoke("getrawtransaction", new Object[] {txHash},Object.class);
	}
	/**
	 * 解码交易16进制字符串到json
	 */
	public JSONObject decoderawtransaction(String txHashHex) throws Throwable{
		Map map = client.invoke("decoderawtransaction", new Object[] {txHashHex},Map.class);
		return new JSONObject(map);
	}
}
