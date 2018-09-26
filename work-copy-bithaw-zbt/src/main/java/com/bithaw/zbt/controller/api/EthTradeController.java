/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午1:58:05
 * @version   V 1.0
 */
package com.bithaw.zbt.controller.api;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.common.annotation.RpcAuthRequire;
import com.bithaw.zbt.service.EthTradeService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月12日 下午1:58:05
 * @version  V 1.0
 */
@Slf4j
@RestController
public class EthTradeController {
	@Autowired
	private EthTradeService ethTradeService;
	
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
	 * @Description v2.0.0 创建eth账户
	 * @method newEthAddress
	 * @param password
	 * @return String 公钥|助记词
	 * @date 2018年9月11日 下午4:38:35
	 */
	@PostMapping(value = "/eth/newEthAccount")
	@RpcAuthRequire
	public String newEthAccount(@Param("password") String password){
		if(StringUtil.isBlank(password)){
			return "";
		}
		return ethTradeService.newAccount(password);
	}
	
	/**
	 * @author WangWei
	 * @Description 创建zbt账户(本质是eth账户)
	 * @method newZbtAddress
	 * @param password
	 * @return 
	 * @return String
	 * @date 2018年9月11日 下午6:33:01
	 */
	@PostMapping(value = "/eth/newZbtAccount")
	@RpcAuthRequire
	public String newZbtAccount(@Param("password") String password){
		if(StringUtil.isBlank(password)){
			return "";
		}
		return ethTradeService.newAccount(password);
	}
	
	/**  
	 * zbt转账接口
	 * @author: WangWei
	 * @date: 2018年8月24日 上午9:59:40 
	 * @version: v1.0.0
	 * @Description: 
	 * @param orderNo 订单号
	 * @param fromAddress 发起交易的账户
	 * @param toAddress
	 * @param amount
	 * @return String JSON字符串
	 */
	@PostMapping(value = "/eth/zbtTrade")
	@RpcAuthRequire
	public String zbtTrade(
			@Param("orderNo") String orderNo,
			@Param("fromAddress") String fromAddress, 
			@Param("toAddress") String toAddress, 
			@Param("amount") String amount//B
			){
		log.info("zbt转账请求参数 orderNo {};toAddress {};amount {}",orderNo,toAddress,amount);
		if(StringUtil.isBlank(orderNo)||StringUtil.isBlank(toAddress)||StringUtil.isBlank(amount)){
			return new JSONBuilder().put("code", 1).put("message","参数错误").build().toJSONString();
		}
		return ethTradeService.zbtTrade(orderNo,fromAddress,toAddress,amount);
	}
	
	
	/**
	 * @author WangWei
	 * @Description 普通交易覆盖
	 * @method zbtTradeCover
	 * @param coverNo
	 * @param orderNo
	 * @param fromAddress
	 * @param toAddress
	 * @param gasPrice
	 * @param amount void
	 * @date 2018年9月14日 下午5:33:17
	 */
	@PostMapping(value = "/eth/cover/zbtTrade")
	@RpcAuthRequire
	public String zbtTradeCover(
			@Param("coverNo") String coverNo,
			@Param("orderNo") String orderNo,
			@Param("fromAddress") String fromAddress, 
			@Param("toAddress") String toAddress, 
			@Param("gasPrice") String gasPrice,
			@Param("amount") String amount//B
			){
		if(StringUtil.isBlank(coverNo) 
				|| StringUtil.isBlank(orderNo) 
				|| StringUtil.isBlank(fromAddress)
				|| StringUtil.isBlank(toAddress)
				|| StringUtil.isBlank(gasPrice)
				|| StringUtil.isBlank(amount)){
			return "ERROR";
		}
		
		return ethTradeService.zbtTradeCover(coverNo, orderNo, gasPrice, toAddress, amount);
	}
}
