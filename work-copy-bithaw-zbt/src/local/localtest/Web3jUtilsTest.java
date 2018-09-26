package localtest;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.apache.commons.codec.binary.Hex;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;

import com.bithaw.zbt.BithawZBTTradeApplication;

@SpringBootTest(classes = BithawZBTTradeApplication.class)
@RunWith(value = SpringJUnit4ClassRunner.class)
public class Web3jUtilsTest {
	
	@Autowired
	Web3j web3j;
	
	public static void newAccount() throws Exception{
		String keyStoreDir = WalletUtils.getDefaultKeyDirectory();
		File file = new File(keyStoreDir);  
		if(!file.exists()){  
		    file.mkdirs();  
		} 
		Bip39Wallet wallet = WalletUtils.generateBip39Wallet("asdaskdnlkj564a6d5",file);
		Credentials credentials = WalletUtils.loadBip39Credentials("asdaskdnlkj564a6d5", wallet.getMnemonic());
		System.out.println(wallet.getMnemonic());
		System.out.println(credentials.getAddress());
		System.out.println(credentials.getEcKeyPair().getPrivateKey().toString(16));
	}
	
	public static void getPrivateKey(){
		String mnemonic = "monkey accident inhale course occur guitar clap pigeon capital salmon profit open";
		String password = "3a95d0fa25f146b3a8bc";
		Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
		String string = credentials.getEcKeyPair().getPrivateKey().toString(16);
		System.out.println(string);
		System.out.println(credentials.getAddress());
	}
	
	public static void getPrivateKey2(){
		Credentials credentials = Credentials.create("00964376b59290052a6791dd3795adb0d5a9fd7b0db0001133ff223a73c7d162da");
		System.out.println(credentials.getAddress());
		byte[] byteArray = credentials.getEcKeyPair().getPrivateKey().toByteArray();
		String encodeHexString = Hex.encodeHexString(byteArray);
		System.out.println(encodeHexString);
	}
	
	public static void createPrikey() throws Exception{
		ECKeyPair createEcKeyPair = Keys.createEcKeyPair();
		createEcKeyPair.getPrivateKey().toString(16);
		WalletFile walletFile = Wallet.createLight("123", createEcKeyPair);
		Credentials credentials = Credentials.create(createEcKeyPair);
	}
	
	public static void main(String[] args) throws Exception {
		getPrivateKey();
	}
}
