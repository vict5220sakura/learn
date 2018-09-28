package com.bithaw.btc.comtroller.api;

import javax.annotation.Resource;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.feign.SysCoinfigClient;
import com.bithaw.btc.service.BtcWalletService;
import com.bithaw.common.annotation.RpcAuthRequire;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 比特币对内接口
 * @author   WangWei
 * @date     2018年8月28日 上午11:05:27
 * @version  V 1.0
 */
@Slf4j
@RestController
public class BtcWalletControllerApi {

	@Resource
	private BtcWalletService btcWalletService;
	
	@Autowired
	private SysCoinfigClient sysCoinfigClient;

	/**
	 * @author WangWei
	 * @Description 
	 * @method newAddress
	 * @return 私钥|公钥
	 * @return String
	 * @date 2018年8月28日 上午11:16:11
	 */
	@PostMapping("/btc/newAddress")
	@RpcAuthRequire
	public String newAddress(){
		log.info("/btc/newAddress接口调用");
		String string = btcWalletService.newAddress();
		return string;
	}
	
	/**
	 * @author WangWei
	 * @Description 转账
	 * @method trade
	 * @param toAddress toAddress
	 * @param amount amount 金额 (单位:比特币)
	 * @return 
	 * @return String json字符串 {"code":"0","message":"","txHash":""}
	 * @date 2018年8月28日 上午11:16:23
	 */
	@PostMapping("/btc/trade")
	@RpcAuthRequire
	public String trade(String toAddress, String amount){
		log.info("/btc/trade接口调用");
		try {
			JSONObject jsonObject = btcWalletService.trade(toAddress, Double.parseDouble(amount));
			log.info("/btc/trade接口调用:调用完成");
			return jsonObject.toString();
		} catch (Throwable e) {
			e.printStackTrace();
			log.error("/btc/trade接口调用:转账失败",e);
			return "";
		}
	}
	
	
	/**
	 * @author WangWei
	 * @Description 查询交易状态
	 * @method getTradeStatus
	 * @param txHash 交易hash
	 * @return 
	 * @return String "FAIL":交易失败/"TRADING":确认中/"SUCCESS":交易成功/"":查询失败
	 * @date 2018年8月28日 上午11:27:07
	 */
	@PostMapping("/btc/getTradeStatus")
	@RpcAuthRequire
	public String getTradeStatus(String txHash){
		log.info("/btc/getTradeStatus接口调用 txhash{}",txHash);
		try {
			if(StringUtil.isBlank(txHash)){
				log.error("/btc/getTradeStatus接口调用失败 : txHash不能为空");
				return "";
			}
			String tradeStatus = btcWalletService.getTradeStatus(txHash);
			log.info("/btc/getTradeStatus接口调用:查询交易状态成功");
			return tradeStatus;
		} catch (Throwable e) {
			log.error("/btc/getTradeStatus接口调用失败",e);
			return "";
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 获取交易详细信息 首先获取交易状态,如果成功在获取交易详细信息
	 * @method getTradeInfo
	 * @param txHash
	 * @return 
	 * @return String json字符串:blockhash 区块hash fees 手续费 height 区块高度 /null
	 * @date 2018年8月28日 上午11:28:09
	 */
	@PostMapping("/btc/getTradeStatusInfo")
	@RpcAuthRequire
	public String getTradeInfo(String txHash){
		log.info("/btc/getTradeStatusInfo接口调用");
		try{
			JSONObject tradeInfo = btcWalletService.getTradeInfo(txHash);
			return tradeInfo.toJSONString();
		}catch(Throwable e){
			log.error("/btc/getTradeStatusInfo接口调用:失败",e);
			return "";
		}
	}
	
	
}
