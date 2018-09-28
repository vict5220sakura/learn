package com.bithaw.btc.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bitcoinj.core.listeners.TransactionConfidenceEventListener;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.FullPrunedBlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.BalanceType;
import org.bitcoinj.wallet.Wallet.SendResult;
import org.bitcoinj.wallet.listeners.KeyChainEventListener;
import org.bitcoinj.wallet.listeners.ScriptsChangeEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.service.BtcWalletService;

public class BtcWalletServiceImpl implements BtcWalletService {

	public Map generate() {
		// NetworkParameters np = new NetworkParameters();
		// Wallet wallet = new Wallet(params);
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		try {
			 //init();
			dumpWallet();
			//kit();
			//sendRequest();
			//balance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void fullPrunedBlockStore () {
		
		
	}
	public static void balance() {/*
	    NetworkParameters params = TestNet3Params.get();
	    String filePrefix = "peer2-testnet";
	    WalletAppKit kit = new WalletAppKit(params, new File("."), filePrefix);
	    // Download the block chain and wait until it's done.
	    kit.startAsync();
	    kit.awaitRunning();
	    String ads = "n1sUgTWv4WqVFhZhh32tyxQbynowkQkpqp";
	    Address address = new Address(params, ads);
	    Wallet wallet = new Wallet(params);
	    wallet.addWatchedAddress(address, 0);
	    System.out.println("wallet.getWatchedAddresses()"+wallet.getWatchedAddresses());
	    BlockChain chain;
	    try {
	        chain = new BlockChain(params, wallet,
	               new MemoryBlockStore(params));
	    	chain = new BlockChain(params, wallet,
		               new SPVBlockStore(params, new File("D:\\bitcoin-blocks")));
	        

	    PeerGroup peerGroup = new PeerGroup(params, chain);
	    peerGroup.addPeerDiscovery(new DnsDiscovery(params));
	    peerGroup.addWallet(wallet);
	    peerGroup.start();
	    //peerGroup.downloadBlockChain();
	    Coin balance = wallet.getBalance();
	    System.out.println("Wallet balance: " + balance);
	    } catch (BlockStoreException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	*/
        NetworkParameters params = TestNet3Params.get();
		ECKey key = new ECKey(Base58.decode("cReExezr81yinmvZziMyXmGtEPbrTZb4hukCQwCebk9gRERJXntx"), Base58.decode("21BFxa5EbLSDewvMpgGiSYuTAevrQTikkrTEtRUZrc5VX"));

        // Now we initialize a new WalletAppKit. The kit handles all the boilerplate for us and is the easiest way to get everything up and running.
        // Have a look at the WalletAppKit documentation and its source to understand what's happening behind the scenes: https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/kits/WalletAppKit.java
        WalletAppKit kit = new WalletAppKit(params, new File("."), key.getPublicKeyAsHex());
        kit.startAsync();
        kit.awaitRunning();
        System.out.println("kit.wallet().getBalance().value="+kit.wallet().getBalance().value);
	}

	private static void init() throws BlockStoreException {
		
		NetworkParameters params = TestNet3Params.get();
		ECKey key = new ECKey();
		System.out.println("We created a new key:\n" + key);
		Address addressFromKey = key.toAddress(params);
		System.out.println("Public Address generated: " + addressFromKey);
		System.out.println("Public key is: " + Base58.encode(key.getPubKey()));
		System.out.println("Private key is: " + key.getPrivateKeyEncoded(params).toString()); 
		Wallet wallet = new Wallet(params);
		wallet.importKey(key);
		File blockFile = new File("D:\\bitcoin-blocks");
		SPVBlockStore blockStore = new SPVBlockStore(params, blockFile);
		BlockChain blockChain = new BlockChain(params, wallet, blockStore);
		PeerGroup peerGroup = new PeerGroup(params, blockChain);
		peerGroup.addPeerDiscovery(new DnsDiscovery(params));
		peerGroup.addWallet(wallet);
		System.out.println("Start peer group");
		peerGroup.start();
		System.out.println("Downloading block chain");
		peerGroup.downloadBlockChain();
		System.out.println("Block chain downloaded");
		// wallet.addCoinsReceivedEventListener(this);
	}
	
	private static void download(Wallet wallet) throws Exception {
		NetworkParameters params = TestNet3Params.get();

		File blockFile = new File("D:\\bitcoin-blocks");
		SPVBlockStore blockStore = new SPVBlockStore(params, blockFile);

		BlockChain blockChain = new BlockChain(params, wallet, blockStore);
		PeerGroup peerGroup = new PeerGroup(params, blockChain);
		peerGroup.addPeerDiscovery(new DnsDiscovery(params));
		peerGroup.addWallet(wallet);

		System.out.println("Start peer group");
		peerGroup.start();

		System.out.println("Downloading block chain");
		peerGroup.downloadBlockChain();
		System.out.println("Block chain downloaded");
		
	}

	private static void dumpWallet() throws Exception {		
		ECKey key = new ECKey(Base58.decode("cReExezr81yinmvZziMyXmGtEPbrTZb4hukCQwCebk9gRERJXntx"), Base58.decode("21BFxa5EbLSDewvMpgGiSYuTAevrQTikkrTEtRUZrc5VX"));
		  // We use the WalletAppKit that handles all the boilerplate for us. Have a look at the Kit.java example for more details.
	    NetworkParameters params = TestNet3Params.get();
	    WalletAppKit kit = new WalletAppKit(params, new File("."), key.getPublicKeyAsHex());
		//System.out.println("the wallet address is "+kit.wallet().currentReceiveAddress().toBase58());
	    kit.startAsync();
	    kit.awaitRunning();
	    System.out.println( "wallet = "+kit.wallet().toString());
	    System.out.println( "wallet balance= "+kit.wallet().getBalance(BalanceType.ESTIMATED).value);
    }
	
	private static void sendRequest() throws Exception {
		ECKey key = new ECKey(Base58.decode("cReExezr81yinmvZziMyXmGtEPbrTZb4hukCQwCebk9gRERJXntx"), Base58.decode("21BFxa5EbLSDewvMpgGiSYuTAevrQTikkrTEtRUZrc5VX"));
		  // We use the WalletAppKit that handles all the boilerplate for us. Have a look at the Kit.java example for more details.
        NetworkParameters params = TestNet3Params.get();
	    WalletAppKit kit = new WalletAppKit(params, new File("."), key.getPublicKeyAsHex());
    	//System.out.println("the wallet address is "+kit.wallet().currentReceiveAddress().toBase58());
        kit.startAsync();
        kit.awaitRunning();
        // How much coins do we want to send?
        // The Coin class represents a monetary Bitcoin value.
        // We use the parseCoin function to simply get a Coin instance from a simple String.
        Coin value = Coin.parseCoin("0.01");
        // To which address you want to send the coins?
        // The Address class represents a Bitcoin address.
        Address to = Address.fromBase58(params, "2N8hwP1WmJrFF5QWABn38y63uYLhnJYJYTF");
        System.out.println("Send money to: " + to.toString());
        System.out.println( "wallet = "+kit.wallet().toString());
        System.out.println( "wallet balance= "+kit.wallet().getBalance(BalanceType.ESTIMATED).value);
        // There are different ways to create and publish a SendRequest. This is probably the easiest one.
        // Have a look at the code of the SendRequest class to see what's happening and what other options you have: https://bitcoinj.github.io/javadoc/0.11/com/google/bitcoin/core/Wallet.SendRequest.html
        // 
        // Please note that this might raise a InsufficientMoneyException if your wallet has not enough coins to spend.
        // When using the testnet you can use a faucet (like the http://faucet.xeno-genesis.com/) to get testnet coins.
        // In this example we catch the InsufficientMoneyException and register a BalanceFuture callback that runs once the wallet has enough balance.
        try {
        	Wallet.SendResult result = kit.wallet().sendCoins(kit.peerGroup(), to, value); 
            //Wallet.SendResult result = kit.wallet().sendCoins(kit.peerGroup(), to, value);
            System.out.println("coins sent. transaction hash: " + result.tx.getHashAsString());
            // you can use a block explorer like https://www.biteasy.com/ to inspect the transaction with the printed transaction hash. 
        } catch (InsufficientMoneyException e) {
            System.out.println("Not enough coins in your wallet. Missing " + e.missing.getValue() + " satoshis are missing (including fees)");
            System.out.println("Send money to: " + kit.wallet().currentReceiveAddress().toString());

            // Bitcoinj allows you to define a BalanceFuture to execute a callback once your wallet has a certain balance.
            // Here we wait until the we have enough balance and display a notice.
            // Bitcoinj is using the ListenableFutures of the Guava library. Have a look here for more information: https://github.com/google/guava/wiki/ListenableFutureExplained
            ListenableFuture<Coin> balanceFuture = kit.wallet().getBalanceFuture(value, BalanceType.AVAILABLE);
            FutureCallback<Coin> callback = new FutureCallback<Coin>() {
                @Override
                public void onSuccess(Coin balance) {
                    System.out.println("coins arrived and the wallet now has enough balance");
                }

                @Override
                public void onFailure(Throwable t) {
                    System.out.println("something went wrong");
                }
            };
            Futures.addCallback(balanceFuture, callback);
        }
	}
	
	private  static void kit() {

        // First we configure the network we want to use.
        // The available options are:
        // - MainNetParams
        // - TestNet3Params
        // - RegTestParams
        // While developing your application you probably want to use the Regtest mode and run your local bitcoin network. Run bitcoind with the -regtest flag
        // To test you app with a real network you can use the testnet. The testnet is an alternative bitcoin network that follows the same rules as main network. Coins are worth nothing and you can get coins for example from http://faucet.xeno-genesis.com/
        // 
        // For more information have a look at: https://bitcoinj.github.io/testing and https://bitcoin.org/en/developer-examples#testing-applications
        NetworkParameters params = TestNet3Params.get();
		ECKey key = new ECKey(Base58.decode("cReExezr81yinmvZziMyXmGtEPbrTZb4hukCQwCebk9gRERJXntx"), Base58.decode("21BFxa5EbLSDewvMpgGiSYuTAevrQTikkrTEtRUZrc5VX"));

        // Now we initialize a new WalletAppKit. The kit handles all the boilerplate for us and is the easiest way to get everything up and running.
        // Have a look at the WalletAppKit documentation and its source to understand what's happening behind the scenes: https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/kits/WalletAppKit.java
        WalletAppKit kit = new WalletAppKit(params, new File("."), key.getPublicKeyAsHex()) {
        	@Override
            protected void onSetupCompleted() {
                // This is called in a background thread after startAndWait is called, as setting up various objects
                // can do disk and network IO that may cause UI jank/stuttering in wallet apps if it were to be done
                // on the main thread.
                    wallet().importKey(key);
            }	
        };
        // In case you want to connect with your local bitcoind tell the kit to connect to localhost.
        // You must do that in reg test mode.
        //kit.connectToLocalHost();

        // Now we start the kit and sync the blockchain.
        // bitcoinj is working a lot with the Google Guava libraries. The WalletAppKit extends the AbstractIdleService. Have a look at the introduction to Guava services: https://github.com/google/guava/wiki/ServiceExplained
        kit.startAsync();
        kit.awaitRunning();

        kit.wallet().addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
            @Override
            public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
                System.out.println("-----> coins resceived: " + tx.getHashAsString());
                System.out.println("received: " + tx.getValue(wallet));
            }
        });

        kit.wallet().addCoinsSentEventListener(new WalletCoinsSentEventListener() {
            @Override
            public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
                System.out.println("coins sent");
            }
        });

        kit.wallet().addKeyChainEventListener(new KeyChainEventListener() {
            @Override
            public void onKeysAdded(List<ECKey> keys) {
                System.out.println("new key added");
            }
        });

/*        kit.wallet().addScriptsChangeEventListener(new ScriptsChangeEventListener() {
            @Override
            public void onScriptsChanged(Wallet wallet, List<Script> scripts, boolean isAddingScripts) {
                System.out.println("new script added");
            }
        });*/

        kit.wallet().addTransactionConfidenceEventListener(new TransactionConfidenceEventListener() {
            @Override
            public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {
                System.out.println("-----> confidence changed: " + tx.getHashAsString());
                TransactionConfidence confidence = tx.getConfidence();
                System.out.println("new block depth: " + confidence.getDepthInBlocks());
            }
        });

        // Ready to run. The kit syncs the blockchain and our wallet event listener gets notified when something happens.
        // To test everything we create and print a fresh receiving address. Send some coins to that address and see if everything works.
        System.out.println("send money to: " + kit.wallet().freshReceiveAddress().toString());

        // Make sure to properly shut down all the running services when you manually want to stop the kit. The WalletAppKit registers a runtime ShutdownHook so we actually do not need to worry about that when our application is stopping.
        //System.out.println("shutting down again");
        //kit.stopAsync();
        //kit.awaitTerminated();
	}

	@Override
	public String newAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject trade(String toAddress, double amount) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTradeStatus(String txHash) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getTradeInfo(String txHash) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkNode() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
