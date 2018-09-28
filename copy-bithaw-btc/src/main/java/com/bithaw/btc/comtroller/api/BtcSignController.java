/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月7日  下午4:09:54
 * @version   V 1.0
 */
package com.bithaw.btc.comtroller.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.service.BtcSignService;
import com.bithaw.common.utils.Rsa2Sign;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 比特币暴露签名接口
 * @author   WangWei
 * @date     2018年9月7日 下午4:09:54
 * @version  V 1.0
 */
@Slf4j
@RestController
public class BtcSignController {
	//@Value("${signserver.privateKey}")
	private String privateKey;
	
	//@Value("${signserver.publicKey}")
	private String publicKey;
	
	@Autowired
	private BtcSignService btcSignService;
	
	@PostMapping("/sign/getAwaitData")
	public String getAwaitData(@Param("timestamp") String timestamp,@Param("sign") String sign){
		log.info("调用比特币离线签名接口,获取待签名的数据");
		try {
			boolean verify = Rsa2Sign.verify(timestamp, sign, publicKey);
			if( !verify ){
				log.info("比特币获取未签名的数据,请求签名验证失败");
				return "";
			}
		} catch (Exception e) {
			log.info("比特币获取未签名的数据,请求签名验证失败,错误信息{}",e);
			return "";
		}
		
		if( System.currentTimeMillis() - Long.parseLong(timestamp) > 15L * 60L * 1000L ){
			log.info("比特币获取未签名的数据,请求超时,错误信息{}");
			return "";
		}
		
		log.info("调用比特币离线签名接口,请求签名验证成功,未超时");
		String jsonString = "";
		try {
			JSONObject returnJson = btcSignService.getAwaitData();
			String returnSign = Rsa2Sign.sign(returnJson.getJSONArray("data").toJSONString().replace(" ", ""), privateKey);
			returnJson.put("sign", returnSign);
			jsonString = returnJson.toJSONString();
		} catch (RuntimeException e) {
			log.error("获取未签名的数据,报错错误信息{}",e);
		}
		return jsonString;
	}
	
	/**
	 * @author WangWei
	 * @Description 设置签名数据,一次一条
	 * @method setRawTransaction
	 * @param json 数据结构{"data":{"uuid":"uuid","rawTransaction":"rawTransaction"},"sign","签名数据"}
	 * @return void
	 * @date 2018年9月7日 下午5:35:05
	 */
	@PostMapping("/sign/setRawTransaction")
	public void setRawTransaction(@Param("json") String json){
		log.info("btc设置签名后的raw数据");
		JSONObject jsonObject = JSONObject.parseObject(json);
		boolean signFlag = false;
		try {
			signFlag = Rsa2Sign.verify(jsonObject.getJSONObject("data").toJSONString().replace(" ", ""), jsonObject.getString("sign"), publicKey);
		} catch (Exception e) {
			log.error("签名验证失败",e);
		}
		
		if( !signFlag ){
			log.error("签名验证失败");
			return;
		}
		
		log.info("btc设置签名后的raw数据,签名验证成功");
		String uuid = Optional.ofNullable(jsonObject)
			.map(p -> p.getJSONObject("data"))
			.map(p -> p.getString("uuid"))
			.orElseThrow(()->new RuntimeException("uuid为空"));
		
		String rawTransaction = Optional.ofNullable(jsonObject)
				.map(p -> p.getJSONObject("data"))
				.map(p -> p.getString("rawTransaction"))
				.orElseThrow(()->new RuntimeException("rawTransaction为空"));
		btcSignService.setRawTransaction(uuid, rawTransaction);
	}
}
