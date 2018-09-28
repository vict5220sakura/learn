/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午5:24:59
 * @version   V 1.0
 */
package com.bithaw.btc.comtroller.api;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.service.BtcSystemService;
import com.bithaw.btc.service.BtcTradeService;
import com.bithaw.common.annotation.RpcAuthRequire;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 比特币对内交易接口
 * @author   WangWei
 * @date     2018年9月5日 下午5:24:59
 * @version  V 1.0
 */
@Slf4j
@RestController
public class BtcTradeController {
	
	@Autowired
	private BtcSystemService btcSystemService;
	@Autowired
	private BtcTradeService btcTradeService;
	
	/**
	 * JSONObject构造器
	 * @author WangWei
	 * @date: 2018年8月24日 上午9:52:30
	 * @version: v1.0.0
	 * @Description:构造者模式构造JSONObject
	 */
	public class JSONBuilder{
		private JSONObject jsonObject;
		JSONBuilder(){
			this.jsonObject = new JSONObject();
		}
		private JSONBuilder put(String key,String value){
			this.jsonObject.put(key, value);
			return this;
		}
		private JSONBuilder put(String key,int value){
			this.jsonObject.put(key, value);
			return this;
		}
		public JSONObject build(){
			return this.jsonObject;
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method newAddress
	 * @return 私钥|公钥
	 * @return String
	 * @date 2018年8月28日 上午11:16:11
	 */
	@PostMapping("/btc/v2/newAddress")
	@RpcAuthRequire
	public String newAddress(){
		log.info("获取比特币公私钥对");
		String address = btcSystemService.createBtcAddress();
		return address;
	}
	
	/**
	 * @author WangWei
	 * @Description 调用接口发起一笔btc提币,存储到数据库
	 * @method tradeSecond
	 * @param toAddress 地址
	 * @param amount 金额(BTC)
	 * @param uuid 查询用uuid
	 * @return String SUCCESS,FAIL,ERROR
	 * @date 2018年9月5日 下午5:11:35
	 */
	@PostMapping("/btc/v2/trade")
	@RpcAuthRequire
	public String trade(String toAddress,String amount,String uuid,String fees){
		if(StringUtil.isBlank(toAddress) || StringUtil.isBlank(amount) || StringUtil.isBlank(uuid)){
			return "ERROR";
		}
		if(StringUtil.isBlank(fees)){
			fees = "0";
		}
		return btcTradeService.trade(toAddress, amount, uuid,fees);
	}
	
	/**
	 * @author WangWei
	 * @Description 查询交易状态
	 * @method getTradeStatus
	 * @param uuid 交易编号
	 * @return String "FAIL":交易失败;"TRADING":确认中;"SUCCESS":交易成功;"ERROR":查询失败
	 * @date 2018年8月28日 上午11:27:07
	 */
	@PostMapping("/btc/v2/getTradeStatus")
	@RpcAuthRequire
	public String getTradeStatus(String uuid){
		log.info("查询交易状态,uuid:{}",uuid);
		if(StringUtil.isBlank(uuid)){
			return "ERROR";
		}
		return btcSystemService.getTradeStatus(uuid);
	}
	
	/**
	 * @author WangWei
	 * @Description 查询交易详情
	 * @method getTradeInfo
	 * @param uuid 交易编号
	 * @return String json数据:{code:0 失败/1 成功;message:说明信息;fees:手续费(BTC)}
	 * @date 2018年8月28日 上午11:27:07
	 */
	@PostMapping("/btc/v2/getTradeInfo")
	@RpcAuthRequire
	public String getTradeInfo(String uuid){
		log.info("查询交易状态,uuid:{}",uuid);
		if(StringUtil.isBlank(uuid)){
			return new JSONBuilder().put("code", "0").put("message","uuid is null").build().toJSONString();
		}
		return btcSystemService.getTradeInfo(uuid);
	}
	
}
