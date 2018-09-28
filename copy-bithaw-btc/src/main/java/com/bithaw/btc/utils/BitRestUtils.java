/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  上午11:39:05
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.entity.UTXO;
import com.bithaw.btc.exception.SendRawtransactionExistException;
import com.bithaw.btc.feign.SysCoinfigClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description Rest方式获取btc节点数据
 * @author   WangWei
 * @date     2018年8月31日 上午11:39:05
 * @version  V 1.0
 */
@Slf4j
@Component
public class BitRestUtils {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	private String btcRpcIp;
	private String btcRpcPort;
	private String btcRpcUsername;
	private String btcRpcPassword;
	private String authorizationKey;
	private String authorizationValue;
	
	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	
	public void init(){
		log.info("初始化开始");
		btcRpcIp = sysCoinfigClient.getSysConfigValue("btc_rpc_ip");
		btcRpcPort = sysCoinfigClient.getSysConfigValue("btc_rpc_port");
		btcRpcUsername = sysCoinfigClient.getSysConfigValue("btc_rpc_user");
		btcRpcPassword = sysCoinfigClient.getSysConfigValue("btc_rpc_password");
		authorizationKey = "Authorization";
		String cred = Base64.encodeBase64String((btcRpcUsername + ":" + btcRpcPassword).getBytes());
		authorizationValue = "Basic " + cred;
		log.info("初始化结束");
	}
	
	/**
	 * @author WangWei
	 * @Description 根据txHash和index返回未使用事务详情
	 * @method gettxout
	 * @param txid 事务hash
	 * @param index 事务index
	 * @return void
	 * @throws Exception 
	 * @throws Throwable 
	 * @date 2018年8月31日 上午10:54:30
	 */
	public UTXO gettxout(String txid,long index) throws Exception {
		log.info("获取一个未使用事务详情 txid {} index {} ",txid,index);
		try {
			JSONObject jsonObjcey = new JSONObject();
			jsonObjcey.put("jsonrpc", "2.0");
			jsonObjcey.put("method", "gettxout");
			jsonObjcey.put("id", 67);
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(txid);
			jsonArray.add((int)index);
			jsonObjcey.put("params", jsonArray);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(authorizationKey, authorizationValue);
			
			HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonObjcey, headers);
			ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity("http://" + btcRpcIp + ":" + btcRpcPort, httpEntity, JSONObject.class);
			JSONObject body = postForEntity.getBody();
			//{"id":67,"result":{"bestblock":"00000000005f00e50149fa4711203ecb0c9ab7ce7cbcb6aeca7337ad6971ab79","confirmations":323,"value":0.05,"scriptPubKey":{"asm":"OP_DUP OP_HASH160 5803802590f0bfa1fe56a18bc95c3c56fa296bb6 OP_EQUALVERIFY OP_CHECKSIG","hex":"76a9145803802590f0bfa1fe56a18bc95c3c56fa296bb688ac","reqSigs":1,"type":"pubkeyhash","addresses":["moYKxABmKjQtiju2KAdK8XrsbQ1dMmzzxu"]},"coinbase":false}}
			JSONObject result = body.getJSONObject("result");
			UTXO build = new UTXO.Builder()//
				.setTxid(txid)//
				.setIndex(String.valueOf(index))//
				.setScriptPubKey(result.getJSONObject("scriptPubKey").getString("hex"))//
				.setConfirmations(result.getString("confirmations"))//
				.setValue(result.getString("value"))//
				.build();
			log.info("获取一个未使用事务详情,结束");
			return build;
		} catch (Throwable e) {
			log.error("获取一个未使用事务详情失败",e);
			throw new Exception(e);
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 访问本地节点,查看一个地址是否存在与本地节点中
	 * @method isExist 
	 * @param address 查询地址
	 * @return boolean 存在与否
	 * @date 2018年9月4日 下午3:54:17
	 */
	public boolean isExist(String address) {
		log.info("查询地址是否存在与本地节点 {}",address);
		Optional.ofNullable(address).orElseThrow(()->new IllegalArgumentException());
		
		JSONObject jsonObjcey = new JSONObject();
		jsonObjcey.put("jsonrpc", "2.0");
		jsonObjcey.put("method", "listaddressgroupings");
		jsonObjcey.put("id", 67);
		JSONArray jsonArray = new JSONArray();
		jsonObjcey.put("params", jsonArray);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(authorizationKey, authorizationValue);
		
		HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonObjcey, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity("http://" + btcRpcIp + ":" + btcRpcPort, httpEntity, JSONObject.class);
		JSONArray addressJsonArray = Optional.ofNullable(postForEntity)
			.map(p -> p.getBody())
			.map(b -> b.getJSONArray("result"))
			.map(r -> r.getJSONArray(0))
			.orElse(new JSONArray());
		for(int i = 0 ; i < addressJsonArray.size() ; i++){
			String addressTemp = addressJsonArray.getJSONArray(i).getString(0);
			if(address.equals(addressTemp)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @author WangWei
	 * @Description 访问本地节点,获取一个地址的全部零钱,需要将地址导入到bitcoin钱包节点中
	 * @method getUtxosByAddress
	 * @param address 地址
	 * @return List<UTXO>
	 * @date 2018年9月4日 下午3:44:57
	 */
	public Optional<List<UTXO>> getUtxosByAddress(String addressStr){
		Optional.ofNullable(addressStr).orElseThrow(() -> new IllegalArgumentException());
		
		JSONObject jsonObjcey = new JSONObject();
		jsonObjcey.put("jsonrpc", "2.0");
		jsonObjcey.put("method", "listunspent");
		jsonObjcey.put("id", 67);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(0);
		jsonArray.add(getHeight().get());
		jsonObjcey.put("params", jsonArray);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(authorizationKey, authorizationValue);
		
		HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonObjcey, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity("http://" + btcRpcIp + ":" + btcRpcPort, httpEntity, JSONObject.class);
		
		Optional<JSONArray> utxoArray = Optional.ofNullable(postForEntity)
			.map(p -> p.getBody())
			.map(p -> p.getJSONArray("result"));
		
		Optional<List<UTXO>> returnList = Optional.ofNullable(new ArrayList<UTXO>());
		for(int i = 0 ; i < utxoArray.orElseGet(() -> new JSONArray()).size() ; i++){
			final int finalI = i;
			Optional<String> filter = utxoArray
				.map(p -> p.getJSONObject(finalI))
				.map(p -> p.getString("address"))
				.filter(p -> p.toUpperCase().equals(addressStr.toUpperCase()));
			
			if(filter.isPresent()){
				JSONObject jsonTemp = utxoArray.get().getJSONObject(i);
				returnList.get().add(new UTXO.Builder()
						.setConfirmations(jsonTemp.getString("confirmations"))
						.setIndex(jsonTemp.getString("vout"))
						.setScriptPubKey(jsonTemp.getString("scriptPubKey"))
						.setTxid(jsonTemp.getString("txid"))
						.setValue(jsonTemp.getString("amount"))
						.setAddress(addressStr)
						.build());
			}
		}
		return returnList;
	}
	
	/**
	 * @author WangWei
	 * @Description 获取区块高度
	 * @method getHeight
	 * @return 
	 * @return Optional<Integer> 
	 * @date 2018年9月4日 下午5:58:37
	 */
	public Optional<Integer> getHeight(){
		log.info("获取区块高度");
		JSONObject jsonObjcey = new JSONObject();
		jsonObjcey.put("jsonrpc", "2.0");
		jsonObjcey.put("method", "getblockcount");
		jsonObjcey.put("id", 67);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(authorizationKey, authorizationValue);
		
		HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonObjcey, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity("http://" + btcRpcIp + ":" + btcRpcPort, httpEntity, JSONObject.class);
		return Optional.ofNullable(postForEntity)
			.map(p -> p.getBody())
			.map(b -> b.getInteger("result"));
	}
	
	/**
	 * @author WangWei
	 * @Description 返回交易确认数,当未找到时返回0
	 * @method getConfirmationsBtTxHash
	 * @param txHash
	 * @return 
	 * @return 
	 * @return Optional<Long>
	 * @date 2018年9月5日 上午10:11:48
	 */
	public Optional<Long> getConfirmationsBtTxHash(String txHash){
		log.info("根据交易hash {} 获取交易确认数",txHash);
		Optional.ofNullable(txHash).orElseThrow(() -> new IllegalArgumentException());
		
		JSONObject jsonObjcey = new JSONObject();
		jsonObjcey.put("jsonrpc", "2.0");
		jsonObjcey.put("method", "gettransaction");
		jsonObjcey.put("id", 67);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(txHash);
		jsonObjcey.put("params", jsonArray);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(authorizationKey, authorizationValue);
		
		HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonObjcey, headers);
		ResponseEntity<JSONObject> postForEntity = null;
		try {
			postForEntity = restTemplate.postForEntity("http://" + btcRpcIp + ":" + btcRpcPort, httpEntity, JSONObject.class);
		} catch (HttpServerErrorException e) {
			Optional<Integer> filter = Optional.ofNullable(e)
				.map(p -> p.getResponseBodyAsString())
				.map(p -> JSONObject.parseObject(p))
				.map(p -> p.getJSONObject("error"))
				.map(p -> p.getInteger("code"))
				.filter(p -> p.equals(Integer.valueOf(-5)));
			if(filter.isPresent()){
				return Optional.ofNullable(Long.valueOf(0L));
			}else{
				throw e;
			}
		}
		return Optional.ofNullable(postForEntity)
				.map(p -> p.getBody())
				.map(p -> p.getJSONObject("result"))
				.map(p -> p.getLong("confirmations"));
	}
	
	/**
	 * @author WangWei
	 * @Description 本地节点广播交易
	 * @method sendRawtransaction
	 * @param rawtransaction
	 * @return Optional<String>
	 * @date 2018年9月6日 下午7:52:12
	 */
	public Optional<String> sendRawtransaction(String rawtransaction){
		log.info("本地节点广播交易 {}",rawtransaction);
		Optional.ofNullable(rawtransaction).orElseThrow(() -> new IllegalArgumentException());
		
		JSONObject jsonObjcey = new JSONObject();
		jsonObjcey.put("jsonrpc", "2.0");
		jsonObjcey.put("method", "sendrawtransaction");
		jsonObjcey.put("id", 67);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(rawtransaction);
		jsonObjcey.put("params", jsonArray);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(authorizationKey, authorizationValue);
		
		HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonObjcey, headers);
		ResponseEntity<JSONObject> postForEntity = null;
		try {
			postForEntity = restTemplate.postForEntity("http://" + btcRpcIp + ":" + btcRpcPort, httpEntity, JSONObject.class);
		} catch (HttpServerErrorException e) {
			Optional<Integer> filter = Optional.ofNullable(e)
				.map(p -> p.getResponseBodyAsString())
				.map(p -> JSONObject.parseObject(p))
				.map(p -> p.getJSONObject("error"))
				.map(p -> p.getInteger("code"))
				.filter(p -> p.equals(Integer.valueOf(-27)));
			if(filter.isPresent()){
				throw new SendRawtransactionExistException("已经存在");
			}else{
				throw e;
			}
		}
		return Optional.ofNullable(postForEntity)
			.map(p -> p.getBody())
			.map(p -> p.getString("result"));
	}
	
	/**
	 * 获取节点连接数 null为报错
	 * @return
	 */
	public Optional<Integer> getConnectionCount(){
		log.info("获取节点对外链接数");
		JSONObject jsonObjcey = new JSONObject();
		jsonObjcey.put("jsonrpc", "2.0");
		jsonObjcey.put("method", "getconnectioncount");
		jsonObjcey.put("id", 67);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(authorizationKey, authorizationValue);
		
		HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonObjcey, headers);
		ResponseEntity<JSONObject> postForEntity = null;
		try {
			postForEntity = restTemplate.postForEntity("http://" + btcRpcIp + ":" + btcRpcPort, httpEntity, JSONObject.class);
		} catch (RestClientException e) {
			log.error("比特币节点链接失败");
			return Optional.empty();
		}
		return Optional.ofNullable(postForEntity)
			.map(p -> p.getBody())
			.map(b -> b.getInteger("result"));
	}
}

