/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月11日  下午3:26:44
 * @version   V 1.0
 */
package com.bithaw.zbt.utils.eth;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description web3j工具类
 * @author   WangWei
 * @date     2018年9月11日 下午3:26:44
 * @version  V 1.0
 */
@Slf4j
@Component
public class EthWeb3jUtil {
	
	/**
	 * @author WangWei
	 * @Description 创建账户
	 * @method newEthAccount
	 * @param password
	 * @throws Exception 
	 * @return String 公钥|助记词
	 * @date 2018年9月11日 下午5:00:37
	 */
	public static String newEthAccount(String password) throws Exception{
		String keyStoreDir = WalletUtils.getDefaultKeyDirectory();
		log.info("keystore路径 {} " ,keyStoreDir);
		File file = new File(keyStoreDir);  
		if(!file.exists()){  
		    file.mkdirs();  
		} 
		Bip39Wallet wallet = WalletUtils.generateBip39Wallet(password,file);
		String mnemonic = wallet.getMnemonic();
		Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);

		String publicAddress = credentials.getAddress();//公钥地址
		log.info("生成账户成功 : " + publicAddress + "|" + mnemonic);
		new File(file,wallet.getFilename()).delete();
		return publicAddress + "|" + mnemonic;
	}
}
