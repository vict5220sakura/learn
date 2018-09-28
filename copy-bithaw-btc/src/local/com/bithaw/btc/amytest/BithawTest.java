package com.bithaw.btc.amytest;

import java.io.File;
import java.math.BigInteger;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.SendResult;
import org.junit.Test;

import com.google.common.util.concurrent.MoreExecutors;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class BithawTest {
//	private NetworkParameters params;
//	private Wallet wallet;
//	private PeerGroup peerGroup;
	
	@Test
	public void test01(){
		NetworkParameters params  = TestNet3Params.get();//比特币测试网络
		ECKey key = new ECKey();
		System.out.println("We created a new key:\n" + key);
		Address addressFromKey = key.toAddress(params);
		System.out.println("Public Address generated: " + addressFromKey);
		System.out.println("Private key is: " + key.getPrivateKeyEncoded(params).toString());
		Wallet wallet = new Wallet(params);
		wallet.importKey(key);
	}
	
	/**
	 * 创建账号
	 */
	@Test
	public void testNmgfrankCom(){
		NetworkParameters params;  // 网络参数声明 //此类用于选择比特币所属网络。
		params = TestNet3Params.get(); // 公共测试网络
//		params = RegTestParams.get(); // 私有测试网络
//		params = MainNetParams.get(); // 生产网络
		
		ECKey ceKey = new ECKey();
		BigInteger privKeyBigInteger = ceKey.getPrivKey(); // 私钥， BigInteger
		log.info("privKeyBigInteger : " + privKeyBigInteger.toString());
		String privateKeyHex = ceKey.getPrivateKeyAsHex(); // 私钥， Hex
		log.info("privateKeyHex : " + privateKeyHex);
		String privateKeyWiF = ceKey.getPrivateKeyAsWiF(params); // 私钥， WIF(Wallet Import Format)
		log.info("privateKeyWiF : " + privateKeyWiF);
		byte[] privKeyBytes = ceKey.getPrivKeyBytes(); // 私钥 byte[]
		
		String publicKeyAsHex = ceKey.getPublicKeyAsHex();// 公钥Hex
		log.info("publicKeyAsHex : " + publicKeyAsHex);
		Address address = ceKey.toAddress(params);
		log.info("Public Address : " + address.toString());
		DumpedPrivateKey privateKeyEncoded = ceKey.getPrivateKeyEncoded(params);
		log.info("Private key endode: " + privateKeyEncoded.toString());
		String addressBase58 = ceKey.toAddress(params).toBase58(); // base58编码后的地址
		log.info("addressBase58 : " + addressBase58);
	}
	
	@Test
	public void trade2(){
		String ownerPrivInBigIntegerStr = "34563289948212156723352594778336742989538232476242233583327468658358139916163";
		BigInteger ownerPrivInBigInteger = new BigInteger(ownerPrivInBigIntegerStr);
		NetworkParameters params = TestNet3Params.get();
		// 构造初始拥有者key
		
		ECKey ownerKey = ECKey.fromPrivate(ownerPrivInBigInteger);
		Address ownerAddress = ownerKey.toAddress(params);
		Transaction tx = new Transaction(params);
		Coin initCoin = Coin.valueOf(1000L);
		tx.addOutput(initCoin, ownerAddress);
		
		Address toAddress = Address.fromBase58(params, "1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F");//接收方地址
		
		Wallet wallet = new Wallet(params);//创建一个钱包
		wallet.importKey(ownerKey);//导入自己的私钥
		
		File blockFile = new File("D:/ww/workSpace/bitcoinjWorkspace/bitcoin-blocks");
		SPVBlockStore blockStore;
		try {
			blockStore = new SPVBlockStore(params, blockFile);
			BlockChain blockChain = new BlockChain(params, wallet, blockStore);
			PeerGroup peerGroup = new PeerGroup(params, blockChain);
			peerGroup.addPeerDiscovery(new DnsDiscovery(params));
			peerGroup.addWallet(wallet);
			peerGroup.start();
			peerGroup.downloadBlockChain();
			 
		} catch (BlockStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
	}
	
	/**
	 * 账户公钥合法性校验
	 */
	@Test
	public void address(){
		try{ 
			NetworkParameters params = MainNetParams.get(); // 生产网络
			Address address = Address.fromBase58(params, "1G9dBLRzmimYjLE5xN2hMX5ubCSygwMh7q");
//			String base58 = address.toBase58();
//			log.info("base58 : " + base58);
		}catch(Exception e){
			log.error("账号合法性校验错误",e);
		}
		
	}
	
	/**
	 * 
	 */
	@Test
	public void privateKey(){
		NetworkParameters params = MainNetParams.get(); // 生产网络
		ECKey ecKey = ECKey.fromPrivate(new BigInteger("34563289948212156723352594778336742989538232476242233583327468658358139916163"));
		String privateKeyAsWiF = ecKey.getPrivateKeyAsWiF(params);
		
		log.info("privateKeyAsWiF : " + privateKeyAsWiF);
	}
	
	/**
	 * 下载btc-bitcoinj部分区块
	 * 成功,但是不知道如何使用
	 * @throws Exception
	 */
	@Test
	public void downBitcoinBlocks() throws Exception{
//		init();
//		log.info("测试 : peerGroup.start()");
//		peerGroup.start();//开始group
//
//		log.info("测试 : peerGroup.downloadBlockChain()");
//		peerGroup.downloadBlockChain();//下载区块
//		log.info("测试 : peerGroup.downloadBlockChain() over");
//		while (true) {
//            Thread.sleep(20);
//        }
	}
	
	/**
	 * 比特币转账3
	 * @throws InsufficientMoneyException 
	 */
	@Test
	public void trade3() throws InsufficientMoneyException{
//		final Coin amountToSend = Coin.parseCoin("0.000001");//设置转账金额
//		Address toAddress = Address.fromBase58(params, "1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F");//设置接收方地址
//		SendResult sendResult = wallet.sendCoins(peerGroup, toAddress, amountToSend);
//		sendResult.broadcastComplete.addListener(new Runnable() {
//			  @Override
//			  public void run() {
//			      System.out.println("测试 : Coins Sent! Transaction hash is " + sendResult.tx.getHashAsString());
//			  }
//			}, MoreExecutors.sameThreadExecutor());
	}
	
	/**
	 * bincoinj转账
	 * @throws Exception 
	 */
	@Test
	public void trade() throws Exception{
//		init();
//		new Thread(){public void run() {
//			try {
//				log.info("测试 : Start peer group");
//				peerGroup.start();//开始group
//
//				log.info("测试 : Downloading block chain");
//				peerGroup.downloadBlockChain();//下载区块
//				log.info("测试 : Block chain downloaded");
//				while (true) {
//		            Thread.sleep(20);
//		        }
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		};}.start();
//		
////		NetworkParameters params = MainNetParams.get(); // 生产网络
//		final Coin amountToSend = Coin.parseCoin("0.000001");//Coin.valueOf(0, 1);
//		
//		
////		String ownerPrivInBigIntegerStr = "34563289948212156723352594778336742989538232476242233583327468658358139916163";
////		BigInteger ownerPrivInBigInteger = new BigInteger(ownerPrivInBigIntegerStr);
////		ECKey ownerKey = ECKey.fromPrivate(ownerPrivInBigInteger);//私钥key
////		
////		Wallet wallet = new Wallet(params);//在生产网络的基础上创建一个钱包
////		wallet.importKey(ownerKey);//钱包导入私钥
//		
//		Address toAddress = Address.fromBase58(params, "1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F");
//		
//		final Wallet.SendResult sendResult = wallet.sendCoins(peerGroup, toAddress, amountToSend);
//
//		sendResult.broadcastComplete.addListener(new Runnable() {
//		  @Override
//		  public void run() {
//		      log.info("测试 : Coins Sent! Transaction hash is " + sendResult.tx.getHashAsString());
//		  }
//		}, MoreExecutors.sameThreadExecutor());
		
		
	}
	
	public void init() throws Exception{
//		params = MainNetParams.get(); // 生产网络
//		
//		String ownerPrivInBigIntegerStr = "34563289948212156723352594778336742989538232476242233583327468658358139916163";
//		BigInteger ownerPrivInBigInteger = new BigInteger(ownerPrivInBigIntegerStr);
//		ECKey ownerKey = ECKey.fromPrivate(ownerPrivInBigInteger);//私钥key
//		
//		wallet = new Wallet(params);//在生产网络的基础上创建一个钱包
//		wallet.importKey(ownerKey);//钱包导入私钥
//		
//		File blockFile = new File("D:/ww/workSpace/bitcoinjWorkspace/bitcoin-blocks");//区块数据
//		SPVBlockStore blockStore = new SPVBlockStore(params, blockFile);//生产网络上结合区块数据
//		BlockChain blockChain = new BlockChain(params, wallet, blockStore);//钱包+生产网络+区块
//		peerGroup = new PeerGroup(params, blockChain);//钱包+生产网络+区块
//		peerGroup.addPeerDiscovery(new DnsDiscovery(params));
//		peerGroup.addWallet(wallet);
	
		
	}

	@Test
	public void getBalance() throws Exception{
//		params = MainNetParams.get(); // 生产网络
//		
//		String ownerPrivInBigIntegerStr = "34563289948212156723352594778336742989538232476242233583327468658358139916163";
//		BigInteger ownerPrivInBigInteger = new BigInteger(ownerPrivInBigIntegerStr);
//		ECKey ownerKey = ECKey.fromPrivate(ownerPrivInBigInteger);//私钥key
//		
//		wallet = new Wallet(params);//在生产网络的基础上创建一个钱包
//		wallet.importKey(ownerKey);//钱包导入私钥
//		
//		File blockFile = new File("D:/ww/workSpace/bitcoinjWorkspace/bitcoin-blocks");//区块数据
//		SPVBlockStore blockStore = new SPVBlockStore(params, blockFile);//生产网络上结合区块数据
//		BlockChain blockChain = new BlockChain(params, wallet, blockStore);//钱包+生产网络+区块
//		peerGroup = new PeerGroup(params, blockChain);//钱包+生产网络+区块
//		peerGroup.addPeerDiscovery(new DnsDiscovery(params));
//		peerGroup.addWallet(wallet);
//		
//		
//		Coin balance = wallet.getBalance();
//		log.info("测试 : " + balance.getValue());
	}
}
