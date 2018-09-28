/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午5:13:35
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import java.util.Optional;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bithaw.btc.feign.SysCoinfigClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午5:13:35
 * @version  V 1.0
 */
@Slf4j
@Component
public class BitcoinjUtil {
	private NetworkParameters params;
	
	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	
	public void init(){
		String NetworkParameters = sysCoinfigClient.getSysConfigValue("bitcoinj_networkparameters");
		
		if("MainNet".equals(NetworkParameters)){
			params = MainNetParams.get();
		}else{
			params = TestNet3Params.get();
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 账号合法性校验
	 * @method checkAddress
	 * @param address
	 * @return boolean
	 * @date 2018年9月5日 下午5:19:49
	 */
	public boolean checkAddress(String address){
		Optional.ofNullable(address).orElseThrow(() -> new IllegalArgumentException());
		try{ 
			Address.fromBase58(params, address);
		}catch(AddressFormatException e){
			log.error("账号合法性校验错误",e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * @author WangWei
	 * @Description 新建比特币公司钥兑
	 * @method newAddress
	 * @return String 私钥|公钥(base58格式)
	 * @date 2018年9月5日 下午5:27:17
	 */
	public String newAddress(){
		log.info("开始新建账户");
		ECKey ceKey = new ECKey();
		String privateKeyWiF = ceKey.getPrivateKeyAsWiF(params); 
		Address address = ceKey.toAddress(params);
		log.info("新建账户结束");
		return privateKeyWiF + "|" + address.toString();
	}
}
