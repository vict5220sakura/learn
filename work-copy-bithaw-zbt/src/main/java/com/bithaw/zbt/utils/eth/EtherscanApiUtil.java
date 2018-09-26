/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午2:34:26
 * @version   V 1.0
 */
package com.bithaw.zbt.utils.eth;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.bithaw.zbt.entity.EthTradeNonce;
import com.bithaw.zbt.feign.SysConfigClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description Etherscan第三方接口工具类
 * @author   WangWei
 * @date     2018年9月12日 下午2:34:26
 * @version  V 1.0
 */
@Slf4j
@Component
public class EtherscanApiUtil {
	@Autowired
	private SysConfigClient sysConfigClient;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private String etherscanApikey;
	private String etherscanUrl;
	
	/**
	 * @author WangWei
	 * @Description 初始化
	 * @method init void
	 * @date 2018年9月12日 下午2:35:06
	 */
	public void init(){
		log.info("初始化开始");
		etherscanApikey = sysConfigClient.getSysConfigValue("etherscan_apikey");
		etherscanUrl = sysConfigClient.getSysConfigValue("etherscan_url");
	}
	
	/**
	 * @author WangWei
	 * @Description 网络etherscan广播一笔交易
	 * {"error":{"code":-32000,"message":"nonce too low"},"id":1,"jsonrpc":"2.0"} 错误信息
	 * {"id":1,"jsonrpc":"2.0","result":"0x5c12f0dc58176eaf05a5058d460aeb62574a38ef258c68f719017e9244d83f07"} 成功信息
	 * @method sendEtherscan
	 * @param ethTradeNonce
	 * @return JSONObject
	 * @date 2018年9月12日 下午2:40:17
	 */
	public JSONObject sendEtherscan(String rawTransaction){
		log.info("网络etherscan广播一笔交易orderNo : {}",rawTransaction);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "proxy");
		map.add("action", "eth_sendRawTransaction");
		map.add("hex", rawTransaction);
		map.add("apikey", etherscanApikey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity(etherscanUrl, request, JSONObject.class);
		return postForEntity.getBody();
	}
	
	/**
	 * @author WangWei
	 * @Description 根据txHash获取交易信息
	 * @method getTransactionByHash
	 * @param txHash
	 * @return ResponseEntity<JSONObject>
	 * @date 2018年9月12日 下午2:43:04
	 */
	public Optional<ResponseEntity<JSONObject>> getTransactionByHash(String txHash){
		String etherscanApikey = sysConfigClient.getSysConfigValue("etherscan_apikey");
		String etherscanUrl = sysConfigClient.getSysConfigValue("etherscan_url");
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "proxy");
		map.add("action", "eth_getTransactionByHash");
		map.add("txhash", txHash);
		map.add("apikey", etherscanApikey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity(etherscanUrl, request, JSONObject.class);
		return Optional.ofNullable(postForEntity);
	}
	
	/**
	 * @author WangWei
	 * @Description etherscan查找区块高度
	 * @method getEtherscanBlockHeight
	 * @return BigInteger
	 * @date 2018年9月12日 下午2:44:25
	 */
	public BigInteger getEtherscanBlockHeight(){
		log.info("etherscan查找区块高度 开始");
		String etherscanApikey = sysConfigClient.getSysConfigValue("etherscan_apikey");
		String etherscanUrl = sysConfigClient.getSysConfigValue("etherscan_url");
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "proxy");
		map.add("action", "eth_blockNumber");
		map.add("apikey", etherscanApikey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity(etherscanUrl, request, JSONObject.class);
		if(postForEntity.getBody().getString("result") != null){
			Long height = Long.parseLong(postForEntity.getBody().getString("result").substring(2), 16);
			log.info("etherscan查找区块高度 成功");
			return new BigInteger(height + "");
		}else{
			log.error("etherscan查找区块高度 异常");
			return null;
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 获取一个地址的交易列表
	 * @method txlist
	 * @param address
	 * @return ResponseEntity<JSONObject>
	 * @date 2018年9月12日 下午2:48:12
	 */
	public ResponseEntity<JSONObject> txlist(String address){
		String etherscanApikey = sysConfigClient.getSysConfigValue("etherscan_apikey");
		String etherscanUrl = sysConfigClient.getSysConfigValue("etherscan_url");
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "account");
		map.add("address", address);
		map.add("action", "txlist");
		map.add("startblock", "0");
		map.add("endblock", "99999999");
		map.add("sort", "desc");
		map.add("apikey", etherscanApikey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		return restTemplate.postForEntity(etherscanUrl, request, JSONObject.class);
	}
	
	/**
	 * @author WangWei
	 * @Description eEtherscan获取一个nonce
	 * @method getNonce
	 * @param address
	 * @return int
	 * @date 2018年9月12日 下午2:52:05
	 */
	public int getNonce(String address) {
		log.info("etherscan获取nonce:开始,address{}",address);
		String etherscanApikey = sysConfigClient.getSysConfigValue("etherscan_apikey");
		String etherscanUrl = sysConfigClient.getSysConfigValue("etherscan_url");
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("module", "proxy");
		map.add("action", "eth_getTransactionCount");
		map.add("address", address);
		map.add("apikey", etherscanApikey);
		map.add("tag", "latest");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity(etherscanUrl, request, JSONObject.class);
		String hex = postForEntity.getBody().getString("result");
		Long parseLong = Long.parseLong(hex.substring(2), 16);
		int nonce = parseLong.intValue();
		log.info("etherscan获取nonce:成功,address{};nonce{}",address,nonce);
		return nonce;
	}
}
