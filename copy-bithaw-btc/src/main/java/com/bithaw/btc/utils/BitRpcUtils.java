package com.bithaw.btc.utils;

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
import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 比特币工具类
 * @author   WangWei
 * @date     2018年8月28日 上午9:20:18
 * @version  V 1.0
 */
@Slf4j
@Component
public class BitRpcUtils {
	private JsonRpcHttpClient client;
	private NetworkParameters params;
	
	/**
	 * @author WangWei
	 * @Description 初始化/重置bit配置
	 * @method init
	 * @param ip 链接地址
	 * @param port 链接端口
	 * @param credUsername Authorization身份认证用户名
	 * @param credPassword Authorization身份认证用户名
	 * @param NetworkParameters 网络标识 "MainNet" "TestNet3"
	 * @return void
	 * @date 2018年8月28日 上午9:20:26
	 */
	public void init(String ip,String port,String credUsername,String credPassword,String NetworkParameters) {
		try {	
			String cred = Base64.encodeBase64String((credUsername + ":" + credPassword).getBytes());
			Map<String, String> headers = new HashMap<String, String>(1);
			headers.put("Authorization", "Basic " + cred);
			client = new JsonRpcHttpClient(new URL("http://" + ip + ":" + port), headers);
			
			if("MainNet".equals(NetworkParameters)){
				params = MainNetParams.get();
			}else{
				params = TestNet3Params.get();
			}
			log.info("初始化bitRpc客户端成功");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.error("初始化bitRpc客户端错误",e);
		}
	}

	/**
	 * @author WangWei
	 * @Description 新建账户
	 * @method newAddress
	 * @return String 私钥|公钥 
	 * @date 2018年8月28日 上午9:21:07
	 */
	public String newAddress(){
		log.info("开始新建账户");
		ECKey ceKey = new ECKey();
		String privateKeyWiF = ceKey.getPrivateKeyAsWiF(params); // 私钥， WIF(Wallet Import Format)
		log.info("privateKeyWiF : " + privateKeyWiF);
		Address address = ceKey.toAddress(params);//公钥 base58
		log.info("Public Address : " + address.toString());
		log.info("新建账户结束");
		return privateKeyWiF + "|" + address.toString();
	}
	
	/**
	 * @author WangWei
	 * @Description 公钥地址合法性校验
	 * @method checkAddress
	 * @param address
	 * @return 
	 * @return boolean
	 * @date 2018年8月28日 上午9:21:20
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
	 * 获取钱包余额
	 * @return
	 */
	public double getBalance() throws Throwable{
		return (double)client.invoke("getbalance", new Object[] {},Object.class);
	}
	
	/**
	 * 获取节点连接数 null为报错
	 * @return
	 */
	public Integer getConnectionCount(){
		log.info("获取节点对外链接数");
		try {
			Integer connectionCount = client.invoke("getconnectioncount", null,Integer.class);
			log.info("获取节点对外链接数 成功");
			return connectionCount;
		} catch (Throwable e) {
			log.error("获取btc全节点连接数错误",e);
			return null;
		}
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
	 * 根据交易hash获取交易16进制字符串
	 */
	public String getrawtransaction(String txHash) throws Throwable{
		return (String)client.invoke("getrawtransaction", new Object[] {txHash},Object.class);
	}
	
	/**
	 * 解码交易16进制字符串到json
	 */
	public Map decoderawtransaction(String txHashHex) throws Throwable{
		Map map = client.invoke("decoderawtransaction", new Object[] {txHashHex},Map.class);
		return map;
	}
	
	/**
	 * 获取交易详细信息
	 * @param txHash
	 * @throws Throwable 
	 */
	public Map gettransaction(String txHash) throws Throwable{
		Map map = client.invoke("gettransaction", new Object[] {txHash},Map.class);
		return map;
	}
	
	/**
	 * 根据区块hash获取区块全部数据(数据量包含全部区块中的交易)
	 */
	public Map getBlockInfo(String blockHash) throws Throwable{
		Map map = client.invoke("getblock",new Object[]{blockHash},Map.class);
		return map;
	}
}