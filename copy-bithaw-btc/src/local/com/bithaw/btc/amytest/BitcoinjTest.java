package com.bithaw.btc.amytest;

import java.io.File;
import java.math.BigInteger;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.SendResult;

import com.google.common.util.concurrent.MoreExecutors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitcoinjTest {
	private static NetworkParameters params;
	private static Wallet wallet;
	private static PeerGroup peerGroup;
	
	//初始化钱包
	public static void init() throws Exception{
		params = MainNetParams.get(); // 接入生产网络
		
		String ownerPrivInBigIntegerStr = "34563289948212156723352594778336742989538232476242233583327468658358139916163";
		BigInteger ownerPrivInBigInteger = new BigInteger(ownerPrivInBigIntegerStr);
		ECKey ownerKey = ECKey.fromPrivate(ownerPrivInBigInteger);//拿到私钥key
		
		wallet = new Wallet(params);//创建一个钱包
		wallet.importKey(ownerKey);//钱包导入一个地址
		
		File blockFile = new File("D:/ww/workSpace/bitcoinjWorkspace/bitcoin-blocks");//区块数据
		SPVBlockStore blockStore = new SPVBlockStore(params, blockFile);//生产网络上结合区块数据
		BlockChain blockChain = new BlockChain(params, wallet, blockStore);//钱包+生产网络+区块
		peerGroup = new PeerGroup(params, blockChain);//钱包+生产网络+区块
		peerGroup.addPeerDiscovery(new DnsDiscovery(params));
		peerGroup.addWallet(wallet);
	}
	
	//开始同步轻区块数据
	public static void downBitcoinBlocks() throws Exception{
		
		log.info("测试 : peerGroup.downloadBlockChain()");
		peerGroup.downloadBlockChain();//下载区块
		log.info("测试 : peerGroup.downloadBlockChain() over");
		while (true) {
            Thread.sleep(20);
        }
	}
	
	//测试发送比特币
	public static void trade() throws InsufficientMoneyException{
		final Coin amountToSend = Coin.parseCoin("0.000001");//设置转账金额
		Address toAddress = Address.fromBase58(params, "1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F");//设置接收方地址
		SendResult sendResult = wallet.sendCoins(peerGroup, toAddress, amountToSend);
		sendResult.broadcastComplete.addListener(new Runnable() {
			  @Override
			  public void run() {
			      System.out.println("测试 : Coins Sent! Transaction hash is " + sendResult.tx.getHashAsString());
			  }
			}, MoreExecutors.sameThreadExecutor());
	}
	
	public static void main(String[] args) throws Exception {
		init();
		log.info("测试 : peerGroup.start()");
		peerGroup.start();//开始group
		new Thread(){public void run() {
			try {
				downBitcoinBlocks();
				trade();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};}.start();
		
		
		
	}
}
