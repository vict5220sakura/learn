package com.bithaw.btc.amytest;

public class BitcoinWalletDemo {
	public static void main(String[] args) throws Throwable {
		CoinUtils2 coinUtils2 = CoinUtils2.getInstance();
//		String newaddress = coinUtils2.getNewaddress();
//		System.out.println(newaddress);
		//2N1J79FV6czY39RMgaj4GXfCpdvx1F9L2mq
		
//		String validateaddress = coinUtils2.validateaddress("2N1JPu9rSSMPnLngZKNzDtaTLXPjoB5thCq");
//		System.out.println(validateaddress);
		
//		String listunspent = coinUtils2.listunspent(6, 999999, "2N1J79FV6czY39RMgaj4GXfCpdvx1F9L2mq");
//		System.out.println(listunspent);
		
		String walletpassphase = coinUtils2.walletpassphase("5462", 60);
		System.out.println(walletpassphase);
	}
}
