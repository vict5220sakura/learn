package com.bithaw.btc.amytest;

import java.io.File;
import java.math.BigInteger;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitcoinjTest2 implements WalletCoinsReceivedEventListener {
	public static void main(String[] args) {
        BitCoinHelloWorld demo = new BitCoinHelloWorld();

        demo.run();
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() throws BlockStoreException {
        NetworkParameters params  = MainNetParams.get();

        //1Nsb1atMXsqkPtagwpGzG1CDusg5HMUF7F
        String ownerPrivInBigIntegerStr = "35416256035178391580114320074052826424377494097673199783360286292417633955292";
		BigInteger ownerPrivInBigInteger = new BigInteger(ownerPrivInBigIntegerStr);
		ECKey ownerKey = ECKey.fromPrivate(ownerPrivInBigInteger);//拿到私钥key


        Wallet wallet = new Wallet(params);
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

        Futures.addCallback(transaction.getConfidence().getDepthFuture(1), new FutureCallback<TransactionConfidence>() {
            public void onSuccess(TransactionConfidence result) {
            	log.info("Transaction confirmed, wallet balance is :" + wallet.getBalance());
            }

            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
