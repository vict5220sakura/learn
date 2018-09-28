/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  上午9:49:42
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.xerces.impl.dv.util.HexBin;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Transaction.SigHash;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bithaw.btc.entity.UTXO;
import com.bithaw.btc.exception.OutlineSignAmountException;
import com.bithaw.btc.feign.SysCoinfigClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月31日 上午9:49:42
 * @version  V 1.0
 */
@Slf4j
@Component
public class BtcOutlineUtils {
	
	/**
	 * checkInputsAmountStrategy : 检查未花费策略,如"local|blockchain|btc",本地|blockchain网站|btc.com网站
	 * 注意: btc在测试网络中无法工作
	 */
	private String checkInputsAmountStrategy;
	
	@Autowired 
	private BitRestUtils bitRestUtils;
	
	@Autowired
	private BlockchainApiUtils blockchainApiUtils;
	
	@Autowired
	private BtcComApiUtils btcComApiUtils;
	
	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	
	/**
	 * bitcoinjNetworkparameters : 网络类型MainNet/TestNet3
	 */
	private String bitcoinjNetworkparameters;
	
	private NetworkParameters params;
	
	public void init(){
		log.info("初始化开始");
		bitcoinjNetworkparameters = sysCoinfigClient.getSysConfigValue("bitcoinj_networkparameters");
		checkInputsAmountStrategy = sysCoinfigClient.getSysConfigValue("btc_check_inputs_amount_strategy");
		if(bitcoinjNetworkparameters.equals("TestNet3")&&checkInputsAmountStrategy.contains("btc")){
			log.error("初始化失败");
		}
		if(bitcoinjNetworkparameters.equals("MainNet")){
			params = MainNetParams.get();
		}else if(bitcoinjNetworkparameters.equals("TestNet3")){
			params = TestNet3Params.get();
		}else{
			throw new RuntimeException("初始化失败");
		}
		log.info("初始化结束");
	}
	
	/**
	 * @author WangWei
	 * @Description 离线签名
	 * @method outLineSign
	 * @param params 网络参数
	 * @param inputs 未花费交易
	 * @param outs 输出地址
	 * @param fees 手续费(比特币)
	 * @throws OutlineSignAmountException 交易错误
	 * @return String 签名后的数据
	 * @date 2018年8月30日 上午11:44:05
	 */
	public String outLineSign(List<Input> inputs, List<Output> outs,String fees) throws OutlineSignAmountException {
		log.info("离线签名");
		try{
			//处理数据
			String ceilingFees = "0.001";//写死手续费上线
			BigDecimal B_8_0 = new BigDecimal( "100000000" );
			BigInteger feesBigInteger = new BigDecimal(fees).multiply(B_8_0).toBigInteger();
			BigInteger ceilingFeesBigInteger = new BigDecimal(ceilingFees).multiply(B_8_0).toBigInteger();
			
			//验证输入数据,输入未花费地址金额正确性
			if( !checkInputsAmount(inputs) ){
				throw new OutlineSignAmountException("输入未花费数据,金额不对");
			}
			
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
				transaction.addInput(Sha256Hash.wrap(input.getInputTxid()), input.getInputIndex(), new Script(HexBin.decode(input.getScriptPubKey())));

				//对输入签名
				ECKey eckey = ECKey.fromPrivate(//
							HexBin.decode(input.getPrivateKeyHex())//
							)//
						; 
				TransactionSignature transactionSignature = transaction.calculateSignature(//
						i//
						, eckey//
						, HexBin.decode(input.getScriptPubKey())//
						, SigHash.SINGLE, true);
				//对输入设置签名
				ScriptBuilder sb = new ScriptBuilder();
				sb.data(transactionSignature.encodeToBitcoin());
				sb.data(eckey.getPubKey());
				Script build = sb.build();
				//transaction.getInput(i).setSequenceNumber(System.currentTimeMillis());
				transaction.getInput(i).setScriptSig(build);
			}
			
			//导出广播16进制字符串
			byte[] bitcoinSerialize = transaction.bitcoinSerialize();
			return HexBin.encode(bitcoinSerialize);
		} catch (OutlineSignAmountException e){
			log.info("离线签名金额验证失败,请检查金额,或验证策略",e);
			throw e;
		} catch (Throwable e){
			log.info("离线签名金额验证失败,异常",e);
			throw new OutlineSignAmountException("离线签名其他异常");
		}
	}

	/**
	 * @author WangWei
	 * @Description 验证输入金额正确性 local|blockchain|btc
	 * @method checkInputsAmount
	 * @param inputs 输入参数
	 * @return boolean 正确与否
	 * @date 2018年8月31日 上午9:57:59
	 */
	private boolean checkInputsAmount(List<Input> inputs) {
		log.info("检查输入金额");
		
		if(checkInputsAmountStrategy == null || "".equals(checkInputsAmountStrategy)){
			return false;
		}
		
		String[] strategys = checkInputsAmountStrategy.split("\\|");
		for (String strategy : strategys) {
			switch (strategy) {
			case "local":
				if ( !checkInputsAmountLocal(inputs) ) {
					log.info("本地节点检查输入,未通过");
					return false;
				}
				break;
			case "blockchain":
				if ( !checkInputsAmountBlockchain(inputs) ) {
					log.info("Blockchain节点检查输入,未通过");
					return false;
				}
				break;
			case "btc":
				if ( !checkInputsAmountBtc(inputs) ) {
					log.info("btc节点检查输入,未通过");
					return false;
				}
				break;
			}
		}
		log.info("检查输入金额,完毕");
		return true;
	}

	/**
	 * @author WangWei
	 * @Description 本地检查一组输入数值
	 * @method checkInputsAmountLocal
	 * @param inputs
	 * @return 
	 * @return boolean
	 * @date 2018年8月31日 上午10:38:12
	 */
	private boolean checkInputsAmountLocal(List<Input> inputs){
		log.info("本地节点检查输入");
		if(inputs == null || inputs.size() == 0){
			return false;
		}
		for(Input input : inputs){
			if( !checkInputsAmountLocal(input) ){
				return false;
			}
			try {
				Thread.sleep(1500L);
			} catch (InterruptedException e) {
				log.error("睡眠2秒错误,请检查");
			}
		}
		log.info("本地节点检查输入,完毕");
		return true;
	}
	/**
	 * @author WangWei
	 * @Description 本地检查一个输入数据
	 * @method checkInputsAmountLocal
	 * @param input
	 * @return 
	 * @return boolean
	 * @date 2018年8月31日 下午4:40:39
	 */
	private boolean checkInputsAmountLocal(Input input){
		try{
			UTXO utxo = bitRestUtils.gettxout(input.getInputTxid(), input.getInputIndex());
			if( new BigDecimal(input.getAmount()).compareTo(new BigDecimal(utxo.getValue())) != 0 ){
				return false;
			}
			return true;
		}catch(Throwable e){
			return false;
		}
	}
	
	/**
	 * @author WangWei
	 * @Description blockchain网站检查一组输入数据
	 * @method checkInputsAmountBlockchain
	 * @param inputs
	 * @return 
	 * @return boolean
	 * @date 2018年8月31日 上午10:38:15
	 */
	private boolean checkInputsAmountBlockchain(List<Input> inputs){
		log.info("Blockchain节点检查输入");
		if(inputs == null || inputs.size() == 0){
			return false;
		}
		for(Input input : inputs){
			if( !checkInputsAmountBlockchain(input) ){
				return false;
			}
			try {
				Thread.sleep(1500L);
			} catch (InterruptedException e) {
				log.error("睡眠2秒错误,请检查");
			}
		}
		return true;
	}
	
	private boolean checkInputsAmountBlockchain(Input input){
		try{
			UTXO utxo = blockchainApiUtils.getUnspent(input.getAddress(), input.getInputTxid(), input.getInputIndex());
			if( new BigDecimal(input.getAmount()).compareTo(new BigDecimal(utxo.getValue())) != 0 ){
				return false;
			}
			return true;
		}catch(Throwable e){
			return false;
		}
	}
	
	/**
	 * @author WangWei
	 * @Description Btc网站检查输入数据
	 * @method checkInputsAmountBtc
	 * @param inputs
	 * @return 
	 * @return boolean
	 * @date 2018年8月31日 上午10:38:16
	 */
	private boolean checkInputsAmountBtc(List<Input> inputs){
		log.info("Btc节点检查输入");
		if(inputs == null || inputs.size() == 0){
			return false;
		}
		for(Input input : inputs){
			if( !checkInputsAmountBtc(input) ){
				return false;
			}
			try {
				Thread.sleep(1500L);
			} catch (InterruptedException e) {
				log.error("睡眠2秒错误,请检查");
			}
		}
		return true;
	}
	private boolean checkInputsAmountBtc(Input input){
		try{
			UTXO utxo = btcComApiUtils.getUnspent(input.getAddress(), input.getInputTxid(), input.getInputIndex());
			if( new BigDecimal(input.getAmount()).compareTo(new BigDecimal(utxo.getValue())) != 0 ){
				return false;
			}
			return true;
		}catch(Throwable e){
			return false;
		}
	}
	
	public static class Input{
		private String inputTxid; //未花费txid
		private long inputIndex;//未花费txid out index
		private String address;//地址
		private String amount;//未花费余额单位(比特币)
		private String confirmations;//确认数
		private String privateKeyHex; //私钥Hex
		private String scriptPubKey; //锁定脚本,固定
		
		public Input(String inputTxid, long inputIndex, String address, String amount, String confirmations, String privateKeyHex, String scriptPubKey) {
			super();
			this.inputTxid = inputTxid;
			this.inputIndex = inputIndex;
			this.address = address;
			this.amount = amount;
			this.confirmations = confirmations;
			this.privateKeyHex = privateKeyHex;
			this.scriptPubKey = scriptPubKey;
		}
		
		public static class Builder{
			private String inputTxid; //未花费txid
			private long inputIndex;//未花费txid out index
			private String address;//地址
			private String amount;//未花费余额单位(比特币)
			private String confirmations;//确认数
			private String privateKeyHex; //私钥Hex
			private String scriptPubKey; //锁定脚本,固定
			
			public Input build(){
				return new Input(inputTxid, inputIndex, address, amount, confirmations, privateKeyHex, scriptPubKey);
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
}
