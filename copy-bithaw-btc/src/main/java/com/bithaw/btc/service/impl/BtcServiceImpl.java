package com.bithaw.btc.service.impl;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.feign.SysCoinfigClient;
import com.bithaw.btc.service.BtcWalletService;
import com.bithaw.btc.utils.BitRpcUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * @Description 比特币service
 * @author   WangWei
 * @date     2018年8月28日 上午10:45:48
 * @version  V 1.0
 */
@Slf4j
@Service
public class BtcServiceImpl implements BtcWalletService {

	@Autowired
	private BitRpcUtils bitRpcUtils;
	
	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	
	/** 
	 * <p>Title: newAddress</p>
	 * <p>Description: </p>
	 * @return 私钥|公钥
	 * @see com.bithaw.btc.service.BtcWalletService#newAddress()  
	 */
	@Override
	public String newAddress() {
		log.info("BtcServiceImpl newAddress() 新建比特币账户开始");
		String newAddress = bitRpcUtils.newAddress();
		log.info("BtcServiceImpl newAddress() 新建比特币账户完成");
		return newAddress;
	}

	/** 
	 * <p>Title: trade</p>
	 * <p>Description: 交易</p>
	 * @param toAddress
	 * @param amount
	 * @return json字符串
	 * @throws Throwable
	 * @see com.bithaw.btc.service.BtcWalletService#trade(java.lang.String, double)  
	 */
	@Override
	public JSONObject trade(String toAddress, double amount) throws Throwable {
		log.info("BtcServiceImpl trade() 转账开始");
		JSONObject returnjsonObject = new JSONObject();
		if(bitRpcUtils.checkAddress(toAddress)){
			
		}else{
			returnjsonObject.put("code", "1");
			returnjsonObject.put("message", "ADDRESS-FAIL");//地址错误
			returnjsonObject.put("txHash", "");
			log.info("BtcServiceImpl trade() 地址错误");
			return returnjsonObject;
		}
		
		if(bitRpcUtils.walletpassphase(sysCoinfigClient.getSysConfigValue("btc_rpc_walletPassword"),//
				Integer.parseInt(sysCoinfigClient.getSysConfigValue("btc_rpc_wallet_unlock_time")))){
			
		}else{
			returnjsonObject.put("code", "2");
			returnjsonObject.put("message", "UNLOCK-FAIL");//钱包解锁失败
			returnjsonObject.put("txHash", "");
			log.info("BtcServiceImpl trade() 钱包解锁失败");
			return returnjsonObject;
		}
		
		if(amount < 0.01){
			if(bitRpcUtils.getBalance() < amount + 0.00001){
				returnjsonObject.put("code", "3");
				returnjsonObject.put("message", "BLANCE-INSUFFICIENT");//账户余额不足
				returnjsonObject.put("txHash", "");
				log.info("BtcServiceImpl trade() 账户余额不足");
				return returnjsonObject; 
			}
		}else{
			if(bitRpcUtils.getBalance() < amount){
				returnjsonObject.put("code", "3");
				returnjsonObject.put("message", "BLANCE-INSUFFICIENT");//账户余额不足
				returnjsonObject.put("txHash", "");
				log.info("BtcServiceImpl trade() 账户余额不足");
				return returnjsonObject;
			}
		}
		
		String txHash = bitRpcUtils.trade(toAddress, amount);
		returnjsonObject.put("code", "0");
		returnjsonObject.put("message", "SUCCESS");//转账成功
		returnjsonObject.put("txHash", txHash);
		log.info("BtcServiceImpl trade() 转账成功");
		return returnjsonObject;
	}

	/** 
	 * <p>Title: getTradeStatus</p>
	 * <p>Description: 获取交易状态</p>
	 * @param txHash
	 * @return FAIL:失败,TRADING:打包中,SUCCESS:成功;
	 * @throws Throwable
	 * @see com.bithaw.btc.service.BtcWalletService#getTradeStatus(java.lang.String)  
	 */
	@Override
	public String getTradeStatus(String txHash) throws Throwable{
		log.info("BtcServiceImpl getTradeStatus()");
		String getrawtransaction = bitRpcUtils.getrawtransaction(txHash);
		if("".equals(getrawtransaction)){
			log.info("BtcServiceImpl getTradeStatus():没有查到交易");
			return "FAIL";
		}
		
		Map transactionMap = bitRpcUtils.gettransaction(txHash);
		JSONObject transactionJSON = new JSONObject(transactionMap);
		Integer confirmations = transactionJSON.getInteger("confirmations");
		
		if(confirmations < 12){
			log.info("BtcServiceImpl getTradeStatus():查到交易 正在确认中");
			return "TRADING";
		}
		log.info("BtcServiceImpl getTradeStatus():查到交易 确认完成");
		return "SUCCESS";
	}

	/**
	 * 获取交易详细信息
	 */
	@Override
	public JSONObject getTradeInfo(String txHash) throws Throwable{
		JSONObject returnJSONObject = new JSONObject();
		log.info("BtcServiceImpl getTradeStatusInfo()");
		Map transactionMap = bitRpcUtils.gettransaction(txHash);
		log.info("BtcServiceImpl getTradeStatusInfo()->bitRpcUtils.gettransaction(txHash):" + transactionMap.toString());
		JSONObject transactionJSONObject = new JSONObject(transactionMap);
		if("".equals(transactionJSONObject.getString("result"))){
			log.info("BtcServiceImpl getTradeStatusInfo():没有查到交易");
			return null;//"BtcServiceImpl getTradeStatusInfo():没有查到交易";
		}
		BigDecimal fee = transactionJSONObject.getBigDecimal("fee");
		String blockhash = transactionJSONObject.getString("blockhash");
		returnJSONObject.put("fees", fee.abs().toPlainString());//手续费
		returnJSONObject.put("blockhash", blockhash);//区块hash
		Map blockInfoMap = bitRpcUtils.getBlockInfo(blockhash);
		JSONObject blockInfoJSON = new JSONObject(blockInfoMap);
		returnJSONObject.put("height", blockInfoJSON.getBigInteger("height").toString());//区块高度
		return returnJSONObject;
	}

	/** 
	 * <p>Title: checkNode</p>
	 * <p>Description: </p>
	 * @return
	 * @see com.bithaw.btc.service.BtcWalletService#checkNode()  
	 */
	@Override
	public boolean checkNode() {
		// TODO Auto-generated method stub
		return false;
	}


	
}
