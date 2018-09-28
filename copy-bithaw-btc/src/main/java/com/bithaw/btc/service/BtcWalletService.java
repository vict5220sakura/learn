package com.bithaw.btc.service;

import com.alibaba.fastjson.JSONObject;

public interface BtcWalletService {
	
	/**
	 * 新建账户
	 * @return 私钥|公钥 
	 * @return "|"分割
	 */
	public String newAddress();
	
	/**
	 * 转账
	 */
	public JSONObject trade(String toAddress,double amount) throws Throwable;
	
	
	/**
	 * 获取交易状态
	 * @throws Throwable 
	 */
	public String getTradeStatus(String txHash) throws Throwable;

	JSONObject getTradeInfo(String txHash) throws Throwable;
	
	boolean checkNode();
}
