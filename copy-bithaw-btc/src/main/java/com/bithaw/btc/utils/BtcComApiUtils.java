/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  下午5:15:14
 * @version   V 1.0
 */
package com.bithaw.btc.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.entity.UTXO;
import com.bithaw.btc.feign.SysCoinfigClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description Btc.com 网站接口api,注意:BTC.com api接口短时间连续访问会出现异常
 * @author   WangWei
 * @date     2018年8月31日 下午5:15:14
 * @version  V 1.0
 */
@Slf4j
@Component
public class BtcComApiUtils {
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	
	/**
	 * btcComMainnetgetUnspentUrl : btc网站获取正式网络未花费交易地址
	 */
	private String btcComMainnetgetUnspentUrl;
	/**
	 * btcComMainnetgetUnspentUrl : btc网站获取正式网络交易详情url,需要替换{address}
	 */
	private String btcComGetTxInfoUrl;
	
	public void init(){
		log.info("初始化开始");
		btcComMainnetgetUnspentUrl = sysCoinfigClient.getSysConfigValue("btcCom_mainnetgetUnspent_url");
		btcComGetTxInfoUrl = sysCoinfigClient.getSysConfigValue("btcCom_getTxInfo_url");
		log.info("初始化结束");
	}
	
	public UTXO getUnspent(String address,String txid,long index) throws Throwable {
		try {
			String url = btcComMainnetgetUnspentUrl.replace("{address}", address);
			ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				log.error("睡眠等待异常");
				throw new RuntimeException("睡眠等待终端异常");
			}
			JSONObject body = responseEntity.getBody();
			JSONArray jsonArray = body.getJSONObject("data").getJSONArray("list");
			for(int i = 0 ; i < jsonArray.size() ; i++){
				JSONObject utxoJson = jsonArray.getJSONObject(i);
				if(utxoJson.getString("tx_hash").equals(txid) && utxoJson.getString("tx_output_n").equals(String.valueOf(index))){
					String value = utxoJson.getString("value");
					value = new BigDecimal(value).divide(new BigDecimal("100000000"), 8, BigDecimal.ROUND_HALF_UP).toPlainString();
					
					UTXO utxo = new UTXO.Builder()//
						.setTxid(txid)//
						.setIndex(String.valueOf(index))//
						.setConfirmations(utxoJson.getString("confirmations"))//
						.setValue(value)//
						.build();
					return utxo;
				}
			}
			throw new Exception("BtcCom未找到");
		} catch (Throwable e) {
			log.error("btcCom查找零钱失败",e);
			throw e;
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 获取交易确认时间
	 * @method getConfirmationsBtTxHash
	 * @param txHash
	 * @return Optional<Long>
	 * @date 2018年9月7日 下午1:52:07
	 */
	public Optional<Long> getConfirmationsBtTxHash(String txHash){
		log.info("btc.com获取交易确认数");
		Optional.ofNullable(txHash).orElseThrow(() -> new IllegalArgumentException());
		String url = btcComGetTxInfoUrl.replace("{txHash}", txHash);
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			log.error("睡眠等待异常");
			throw new RuntimeException("睡眠等待终端异常");
		}
		return Optional.ofNullable(responseEntity)
			.map(p -> p.getBody())
			.map(p -> JSONObject.parseObject(p))
			.map(p -> p.getJSONObject("data"))
			.map(p -> p.getLong("confirmations"));
	}
	
	/**
	 * @author WangWei
	 * @Description 查找全部零钱
	 * @method getUTXOs 获取一个地址未使用的零钱
	 * @param address
	 * @return Optional<List<UTXO>>
	 * @date 2018年9月7日 下午2:56:35
	 */
	public Optional<List<UTXO>> getUTXOs(String address){
		log.info("BTCCOM获取零钱");
		Optional.ofNullable(address).orElseThrow(() -> new IllegalArgumentException());
		String url = btcComMainnetgetUnspentUrl.replace("{address}", address);
		ResponseEntity<String> data = restTemplate.getForEntity(url, String.class);
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			log.error("睡眠等待异常");
			throw new RuntimeException("睡眠等待终端异常");
		}
		JSONArray jsonArray = Optional.ofNullable(data)
			.map(p -> p.getBody())
			.map(p -> JSONObject.parseObject(p))
			.map(p -> p.getJSONObject("data"))
			.map(p -> p.getJSONArray("list"))
			.orElseGet(() -> new JSONArray());
		
		ArrayList<UTXO> returnList = new ArrayList<UTXO>();
		for(int i = 0 ; i < jsonArray.size() ; i++){
			String amount = jsonArray.getJSONObject(i).getString("value");
			String amountBtc = new BigDecimal(amount).divide(new BigDecimal("100000000"), 8, BigDecimal.ROUND_HALF_UP).toPlainString();
			String scriptPubKey = getScriptPubKey(jsonArray.getJSONObject(i).getString("tx_hash"),jsonArray.getJSONObject(i).getString("tx_output_n"));
			UTXO utxo = new UTXO.Builder()
				.setAddress(address)
				.setConfirmations(jsonArray.getJSONObject(i).getString("confirmations"))
				.setIndex(jsonArray.getJSONObject(i).getString("tx_output_n"))
				.setTxid(jsonArray.getJSONObject(i).getString("tx_hash"))
				.setValue(amountBtc)
				.setScriptPubKey(scriptPubKey)
				.build();
			returnList.add(utxo);
		}
		return Optional.ofNullable(returnList);
	}

	/**
	 * @author WangWei
	 * @Description 查找交易详情获取锁定脚本
	 * @method getScriptPubKey
	 * @param string
	 * @param string2
	 * @return String
	 * @date 2018年9月7日 下午3:13:05
	 */
	private String getScriptPubKey(String txid, String index) {
		log.info("BTCCOM获取锁定脚本");
		Optional.ofNullable(txid).orElseThrow(() -> new IllegalArgumentException());
		String url = btcComGetTxInfoUrl.replace("{txHash}", txid);
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			log.error("睡眠等待异常");
			throw new RuntimeException("睡眠等待终端异常");
		}
		return Optional.ofNullable(responseEntity)
			.map(p -> p.getBody())
			.map(p -> JSONObject.parseObject(p))
			.map(p -> p.getJSONObject("data"))
			.map(p -> p.getJSONArray("outputs"))
			.map(p -> p.getJSONObject(Integer.parseInt(index)))
			.map(p -> p.getString("script_hex"))
			.orElseThrow(()->new RuntimeException("BTC寻找锁定脚本失败"));
	}
}
