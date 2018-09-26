package localtest;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

public class TestSolidityMethod {
	
	/**
	 * 销毁币
	 */
	@Test
	public void xiahui(){
		String mnemonic = "idea hire door road hybrid business steak victory kangaroo notice actress motion";
		String password = "123";
		String solidityAddress = "0xb00ecbd39b5138f9eb7680205f565848b3699742";
		BigInteger nonce = new BigInteger("63");
		BigInteger gasPrice = new BigInteger("6000000000");
		BigInteger gasLimit = BigInteger.valueOf(500000L);
		
		String data = FunctionEncoder.encode(//
				new Function(//
						"burn"//
						, Arrays.asList(//
								new Uint256(new BigInteger("1"))//
								)//
						, Arrays.asList(//
								new TypeReference<Bool>() {}//
								)//
						)//
				);
		
    	Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        RawTransaction rawTransaction  = RawTransaction.createTransaction(//
        		nonce//
        		,gasPrice//
        		,gasLimit//
        		,solidityAddress//
        		,new BigInteger("0")//
        		,data//
        		);//可以额外带数据
        
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        System.out.println(hexValue);
	}
	
	/**
	 * 权限移交
	 * 
	 */
	@Test
	public void changeAdmin(){
		String mnemonic = "idea hire door road hybrid business steak victory kangaroo notice actress motion";
		String password = "123";
		String solidityAddress = "0xb00EcbD39B5138F9EB7680205F565848B3699742";
		String toAddress = "0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b";
		BigInteger nonce = new BigInteger("65");
		BigInteger gasPrice = new BigInteger("6000000000");
		BigInteger gasLimit = BigInteger.valueOf(500000L);
		
		String data = FunctionEncoder.encode(//
				new Function(//
						"transferOwnership"//
						, Arrays.asList(//
								new Address(toAddress)//
								)//
						, Arrays.asList(//
								//
								)//
						)//
				);
    	Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        RawTransaction rawTransaction  = RawTransaction.createTransaction(//
        		nonce//
        		,gasPrice//
        		,gasLimit//
        		,solidityAddress//
        		,new BigInteger("0")//
        		,data//
        		);//可以额外带数据
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        System.out.println(hexValue);
	}
	
	@Test
	public void testt(){
		String mnemonic = "twelve intact race grit slender airport achieve need ahead rotate gravity perfect";
		String password = "ad1ddda4be1247b3b973";
		Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
		String string = credentials.getEcKeyPair().getPrivateKey().toString(16);
		System.out.println(string);
	}
	public static void main(String[] args) {
		String mnemonic = "twelve intact race grit slender airport achieve need ahead rotate gravity perfect";
		String password = "ad1ddda4be1247b3b973";
		Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
		String string = credentials.getEcKeyPair().getPrivateKey().toString(16);
		System.out.println(string);
	}
	
	
}
