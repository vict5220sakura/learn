package localtest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import com.bithaw.zbt.BithawZBTTradeApplication;
@SpringBootTest(classes = BithawZBTTradeApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class Web3SendTest {
	@Autowired
	Web3j web3j;
	
	public static void sendSignTracation(){
		String fromAddress = "0x763b78bc83ef328a252b25b56ba2a7fe49774b72";
		String mnemonic = "idea hire door road hybrid business steak victory kangaroo notice actress motion";
		String password = "123";
		String toAddress = "0x35525353c1bde270c289631075b69758519fb45b";
		String solidityAddress = "0xb00ecbd39b5138f9eb7680205f565848b3699742";
		BigInteger nonce = new BigInteger("83");
		BigInteger value = new BigInteger("100000000");
		BigInteger gasPrice = new BigInteger("6000000000");
		BigInteger gasLimit = BigInteger.valueOf(500000L);
		
		String data = FunctionEncoder
				.encode(new Function("transfer", Arrays.asList(new Address(toAddress), new Uint256(value)),
						Arrays.asList(new TypeReference<Bool>() {
						})));
		System.out.println(data);
    	Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        RawTransaction rawTransaction  = RawTransaction.createTransaction(//
        		nonce,gasPrice,gasLimit,solidityAddress,new BigInteger("0"),data);//可以额外带数据
        
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        System.out.println("签名1 "+hexValue);
	}
	
	@Test
	public void testsend() throws IOException, InterruptedException, ExecutionException {
		EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction("0xf8aa45850165a0bc008307a12094b00ecbd39b5138f9eb7680205f565848b369974280b844a9059cbb0000000000000000000000004fe415ddf8451c9de3f89d8f815f0d014fbe567b00000000000000000000000000000000000000000000000000000000000000011ba07f9cea3a05a47ae4e00c7cb6d27b46f43b18682e4975cc919fbbe5dfc3bda2d2a01e16a83358e7a6259b964477fecde60b9e1124828388df9d7303c291f05344d0")//
									.sendAsync().get();
		System.out.println(ethSendTransaction.hasError());
		if(ethSendTransaction.hasError()){
			System.out.println(ethSendTransaction.getError().getMessage());
			System.out.println(ethSendTransaction.getError().getCode());
		}else{
			System.out.println(ethSendTransaction.getTransactionHash());
		}
	}
	
	public static void sendViewTracation(){
		
	}
	
	public static void main(String[] args) {
		sendSignTracation();
	}
	
	
}
