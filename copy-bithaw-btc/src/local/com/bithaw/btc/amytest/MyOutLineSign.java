/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月29日  下午5:49:29
 * @version   V 1.0
 */
package com.bithaw.btc.amytest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptChunk;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi.RIPEMD160;
import org.junit.Test;
import org.springframework.security.crypto.codec.Hex;

import com.bithaw.btc.exception.OutlineSignAmountException;


/**
 * @Description
 * @author WangWei
 * @date 2018年8月29日 下午5:49:29
 * @version V 1.0
 */
public class MyOutLineSign {

	public static class Input{
		private String inputTxid; //未花费txid
		private long inputIndex;//未花费txid out index
		private String address;//地址
		private String amount;//未花费余额单位(比特币)
		private String confirmations;//确认数
		private String privateKeyHex; //私钥Hex
		private String scriptPubKey; //锁定脚本,固定
		private String scriptSig; //解锁脚本,指定
		
		public Input(String inputTxid, long inputIndex, String address, String amount, String confirmations, String privateKeyHex, String scriptPubKey, String scriptSig) {
			super();
			this.inputTxid = inputTxid;
			this.inputIndex = inputIndex;
			this.address = address;
			this.amount = amount;
			this.confirmations = confirmations;
			this.privateKeyHex = privateKeyHex;
			this.scriptPubKey = scriptPubKey;
			this.scriptSig = scriptSig;
		}
		public static class Builder{
			private String inputTxid; //未花费txid
			private long inputIndex;//未花费txid out index
			private String address;//地址
			private String amount;//未花费余额单位(比特币)
			private String confirmations;//确认数
			private String privateKeyHex; //私钥Hex
			private String scriptPubKey; //解锁脚本,指定
			private String scriptSig; //解锁脚本,指定
			public Input build(){
				return new Input(inputTxid, inputIndex, address, amount, confirmations, privateKeyHex, scriptPubKey, scriptSig);
			}
			public Builder setInputTxid(String inputTxid) {
				this.inputTxid = inputTxid;
				return this;
			}
			public Builder setInputIndex(long inputIndex) {
				this.inputIndex = inputIndex;
				return this;
			}
			public Builder setPrivateKeyHex(String privateKeyHex) {
				this.privateKeyHex = privateKeyHex;
				return this;
			}
			public Builder setScriptPubKey(String scriptPubKey) {
				this.scriptPubKey = scriptPubKey;
				return this;
			}
			public Builder setAmount(String amount) {
				this.amount = amount;
				return this;
			}
			public Builder setConfirmations(String confirmations) {
				this.confirmations = confirmations;
				return this;
			}
			public Builder setAddress(String address) {
				this.address = address;
				return this;
			}
			public Builder setScriptSig(String scriptSig) {
				this.scriptSig = scriptSig;
				return this;
			}
		}
		public String getInputTxid() {
			return inputTxid;
		}
		public void setInputTxid(String inputTxid) {
			this.inputTxid = inputTxid;
		}
		public long getInputIndex() {
			return inputIndex;
		}
		public void setInputIndex(long inputIndex) {
			this.inputIndex = inputIndex;
		}
		public String getPrivateKeyHex() {
			return privateKeyHex;
		}
		public void setPrivateKeyHex(String privateKeyHex) {
			this.privateKeyHex = privateKeyHex;
		}
		public String getScriptPubKey() {
			return scriptPubKey;
		}
		public void setScriptPubKey(String scriptPubKey) {
			this.scriptPubKey = scriptPubKey;
		}
		/**
		 * @author WangWei
		 * @Description 单位(比特币)
		 * @method getAmount
		 * @return String 单位(比特币)
		 * @date 2018年8月30日 上午11:19:37
		 */
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getConfirmations() {
			return confirmations;
		}
		public void setConfirmations(String confirmations) {
			this.confirmations = confirmations;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getScriptSig() {
			return scriptSig;
		}
		public void setScriptSig(String scriptSig) {
			this.scriptSig = scriptSig;
		}
	}

	public static class Output{
		private String address;
		private String amount;//单位(比特币)
		
		public Output(String address, String amount) {
			super();
			this.address = address;
			this.amount = amount;
		}
		
		public static class Builder{
			private String address;
			private String amount;//单位比特币
			public Output build(){
				return new Output(address, amount);
			}
			public Builder setAddress(String address) {
				this.address = address;
				return this;
			}
			public Builder setAmount(String amount) {
				this.amount = amount;
				return this;
			}
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		/**
		 * @author WangWei
		 * @Description 
		 * @method getAmount
		 * @return String 单位(比特币)
		 * @date 2018年8月30日 下午12:01:58
		 */
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 离线签名
	 * @method outLineSign
	 * @param params 网络参数
	 * @param inputs 未花费交易
	 * @param outs 输出地址
	 * @param fees 手续费(比特币)
	 * @param ceilingFees 手续费上限(比特币)
	 * @throws OutlineSignAmountException 交易错误
	 * @return String 签名后的数据
	 * @date 2018年8月30日 上午11:44:05
	 */
	public static String outLineSign(NetworkParameters params,List<Input> inputs, List<Output> outs,String fees,String ceilingFees) throws OutlineSignAmountException {
		try{
			BigDecimal B_8_0 = new BigDecimal( "100000000" );
			BigInteger feesBigInteger = new BigDecimal(fees).multiply(B_8_0).toBigInteger();
			BigInteger ceilingFeesBigInteger = new BigDecimal(ceilingFees).multiply(B_8_0).toBigInteger();
			
			//验证手续费
			BigInteger inputAmount = new BigInteger("0");
			BigInteger outputAmount = new BigInteger("0");
			for(Input input : inputs){
				BigInteger amount = new BigDecimal( input.getAmount() ).multiply(B_8_0).toBigInteger();
				inputAmount = inputAmount.add(amount);
			}
			for(Output output : outs){
				BigInteger amount = new BigDecimal( output.getAmount() ).multiply(B_8_0).toBigInteger();
				outputAmount = outputAmount.add(amount);
			}
			if( outputAmount.add(feesBigInteger) .compareTo(inputAmount) != 0 ){
				throw new OutlineSignAmountException("交易手续费设置错误");
			}
			if( feesBigInteger.compareTo( ceilingFeesBigInteger ) > 0 ){
				throw new OutlineSignAmountException("交易手续费超过预警值");
			}
			
			//构建交易
			Transaction transaction = new Transaction(params);
			transaction.setVersion(2);
			
			//设置输出
			for (Output output : outs) {
				transaction.addOutput(//
								Coin.valueOf( new BigDecimal(output.getAmount()).multiply(B_8_0).longValue() )//
								, Address.fromBase58(params, output.getAddress())//
						);
			}
			
			//设置输入
			for(int i = 0 ; i < inputs.size() ; i++){
				Input input = inputs.get(i);
				
				//添加一个输入
				transaction.addInput(Sha256Hash.wrap(input.getInputTxid()), input.getInputIndex(), new Script(Hex.decode(input.getScriptPubKey())));

				//对输入签名
				ECKey privateKey = ECKey.fromPrivate(//
								Hex.decode(input.getPrivateKeyHex())//
								)//
						; 
				TransactionSignature transactionSignature = transaction.calculateSignature(//
						i//
						, privateKey//
						, Hex.decode(input.getScriptSig())//
						, Transaction.SigHash.ALL, true);
				//对输入设置签名
				ScriptBuilder sb = new ScriptBuilder();
				sb.data(transactionSignature.encodeToBitcoin());
				sb.data(privateKey.getPubKey());
				Script build = sb.build();
				transaction.getInput(i).setScriptSig(build);
			}
			
			//导出广播16进制字符串
			byte[] bitcoinSerialize = transaction.bitcoinSerialize();
			return new String(Hex.encode(bitcoinSerialize));
		} catch (OutlineSignAmountException e){
			throw e;
		} catch (Throwable e){
			throw new OutlineSignAmountException("离线签名其他异常");
		}
	}

	@Test
	public void outLineSignTest() throws OutlineSignAmountException {
		ArrayList<Input> inputs = new ArrayList<Input>();
		inputs.add(new Input.Builder()//
				.setAddress("1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F")
				.setInputTxid("c978e4750b86e051775093e640a4f259327dc57d2bd504687083498d871c132b")//
				.setInputIndex(0L)//
				.setAmount("0.00199")//
				.setScriptPubKey("76a914efebd4690d7635a6fd826b70a62f945c52ddeebe88ac")//
				.setPrivateKeyHex("4e4ce3fb3335f38a65939e7fdc89dfac6ad60f48b1021c3b61b374bb7df5addc")//
				.build());
//		inputs.add(new Input.Builder()//
//				.setAddress("moYKxABmKjQtiju2KAdK8XrsbQ1dMmzzxu")
//				.setInputTxid("0427f7ae4b71aba9ffeca93aa2ec343820629715652ec6767f2628d6a39318b4")//
//				.setInputIndex(1L)//
//				.setAmount("0.1")//
//				.setScriptPubKey("76a9145803802590f0bfa1fe56a18bc95c3c56fa296bb688ac")//
//				.setPrivateKeyHex("72fb9545ca6da763b3f577e0a605f868325c1dfd01942f4e3550c79d5def85a5")//
//				.build());
		
		List<Output> outs = new ArrayList<Output>();
		outs.add(new Output.Builder()//
				.setAddress("1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F")//
				.setAmount("0.00198")//
				.build());
		
		String outLineSign = outLineSign(MainNetParams.get(), inputs, outs, "0.00001", "0.00001");
		System.out.println(outLineSign);
	}
}
