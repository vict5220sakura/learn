package com.bithaw.btc.amytest;

import java.io.File;
import java.math.BigInteger;
import java.util.Optional;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitCoinHelloWorld implements WalletCoinsReceivedEventListener {

    public static void main(String[] args) {
        BitCoinHelloWorld demo = new BitCoinHelloWorld();

        demo.run();
        
        JSONObject jsonObject = new JSONObject();
    }

    public void run() {
        try {
            init();

            log.info("Waiting for coins...");

            while (true) {
                Thread.sleep(20);
            }
        } catch (BlockStoreException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (InterruptedException e) {
        	e.printStackTrace();
        	log.error(e.getMessage());
        }
    }

	private void init() throws BlockStoreException {
		NetworkParameters params = MainNetParams.get(); // 生产网络
        String ownerPrivInBigIntegerStr = "35416256035178391580114320074052826424377494097673199783360286292417633955292";
		BigInteger ownerPrivInBigInteger = new BigInteger(ownerPrivInBigIntegerStr);
        ECKey ownerKey = ECKey.fromPrivate(ownerPrivInBigInteger);
		Address ownerAddress = ownerKey.toAddress(params);
//		Transaction tx = new Transaction(params);
//		Coin initCoin = Coin.valueOf(1000L);
//		tx.addOutput(initCoin, ownerAddress);
		
		Address toAddress = Address.fromBase58(params, "1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F");//接收方地址
		
		Wallet wallet = new Wallet(params);//创建一个钱包



        wallet.importKey(ownerKey);

        File blockFile = new File("D:/ww/workSpace/bitcoinjWorkspace/bitcoin-blocks");
        SPVBlockStore blockStore = new SPVBlockStore(params, blockFile);

        BlockChain blockChain = new BlockChain(params, wallet, blockStore);
        PeerGroup peerGroup = new PeerGroup(params, blockChain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        peerGroup.addWallet(wallet);

        log.info("Start peer group");
        peerGroup.start();

        log.info("Downloading block chain");
        peerGroup.downloadBlockChain();
        log.info("Block chain downloaded");

        wallet.addCoinsReceivedEventListener(this);
    }


    @Override
    public void onCoinsReceived(final Wallet wallet, final Transaction transaction, Coin prevBalance, Coin newBalance) {
        final Coin value = transaction.getValueSentToMe(wallet);

        log.info("Received tx for " + value.toFriendlyString() + ": " + transaction);

        log.info("Previous balance is " + prevBalance.toFriendlyString());

        log.info("New estimated balance is " + newBalance.toFriendlyString());

        log.info("Coin received, wallet balance is :" + wallet.getBalance());
        try {
			Thread.sleep(10000000000000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//
//        Futures.addCallback(transaction.getConfidence().getDepthFuture(1), new FutureCallback<TransactionConfidence>() {
//            public void onSuccess(TransactionConfidence result) {
//                System.out.println("Transaction confirmed, wallet balance is :" + wallet.getBalance());
//            }
//
//            public void onFailure(Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }


}