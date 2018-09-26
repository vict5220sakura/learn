/**
 * @Description 签名接口
 * @author  WangWei
 * @Date    2018年8月27日  下午3:16:22
 * @version   V 1.0
 */
package com.bithaw.zbt.controller.api;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.common.utils.Rsa2Sign;
import com.bithaw.zbt.service.SignService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 签名接口
 * @author   WangWei
 * @date     2018年8月27日 下午3:16:22
 * @version  V 1.0
 */
@Slf4j
@RestController
public class SignController {
	
	//@Value("${signserver.privateKey}")
	private String privateKey;
	
	//@Value("${signserver.publicKey}")
	private String publicKey;
	
	@Autowired
	private SignService signService;
	
	/**
	 * @author WangWei
	 * @Description 获取待签名数据
	 * @method getAwaitData
	 * @param timestamp 时间戳字符串 和系统时间比较,超过15分钟判定为超时
	 * @param sign 签名
	 * @return 
	 * @return String json字符串,sign采用urlencode编码{data:[{},{}],sign:""} 签名数据data不带空格 启动value单位为eth gasPrice单位为gwei
	 * @date 2018年8月27日 下午5:14:09
	 */
	@PostMapping("/sign/getAwaitData")
	public String getAwaitData(@Param("timestamp") String timestamp,@Param("sign") String sign){
		log.info("获取未签名的数据");
		try {
			boolean verify = Rsa2Sign.verify(timestamp, sign, publicKey);
			if( !verify ){
				log.info("获取未签名的数据,请求签名验证失败");
				return "";
			}
		} catch (Exception e) {
			log.info("获取未签名的数据,请求签名验证失败,错误信息{}",e);
			return "";
		}
		log.info("获取未签名的数据,签名验证成功");
		String jsonString = "";
		try {
			if( System.currentTimeMillis() - Long.parseLong(timestamp) > 15L * 60L * 1000L )
				return "";
			JSONObject returnJson = signService.getAwaitData();
			String returnSign = Rsa2Sign.sign(returnJson.getJSONArray("data").toJSONString().replace(" ", ""), privateKey);
			returnJson.put("sign", returnSign);
			jsonString = returnJson.toJSONString();
		} catch (Throwable e) {
			log.error("获取未签名的数据,报错错误信息{}",e);
		}
		log.info("获取未签名的数据,成功");
		return jsonString;
	}
	
	/**
	 * @author WangWei
	 * @Description 设置签名后的数据 一次一条,
	 * @method setRawTransaction
	 * @param json 包含orderNo rawTransaction ,{"orderNo":"","rawTransaction":""}
	 * @param sign 延签数据去除空格
	 * @return void
	 * @date 2018年8月27日 下午5:48:17
	 */
	@PostMapping("/sign/setRawTransaction")
	public void setRawTransaction(@Param("json") String json,@Param("sign") String sign){
		log.info("设置签名后的数据");
		
		boolean verify = false;
		try {
			verify = Rsa2Sign.verify(json.replace(" ", ""), sign, publicKey);
			
		} catch (Exception e) {
			log.error("签名验证失败",e);
		}
		
		if( !verify ){
			log.error("签名验证失败");
			return;
		}
		
		log.info("设置签名后的数据,签名验证成功");
		JSONObject parseObject = JSONObject.parseObject(json);
		String orderNo = parseObject.getString("orderNo");
		String rawTransaction = parseObject.getString("rawTransaction");
		if(StringUtils.isBlank(orderNo)){
			log.info("设置签名后的数据,失败,order参数为空");
			return;
		}
		if(StringUtils.isBlank(rawTransaction)){
			log.info("设置签名后的数据,失败,rawTransaction参数为空");
			return;
		}
		signService.setRawTransaction(orderNo, rawTransaction);
		log.info("设置签名后的数据,成功");
	}
}
