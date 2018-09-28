/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月31日  下午4:42:48
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
 * @Description Blockchain网站接口api
 * @author   WangWei
 * @date     2018年8月31日 下午4:42:48
 * @version  V 1.0
 */
@Slf4j
@Component
public class BlockchainApiUtils {
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	
	/**
	 * blockchainTestnetGetUnspentUrl : blockchain网站获取测试网络未花费零钱url
	 */
	private String blockchainTestnetGetUnspentUrl;
	/**
	 * blockchainMainnetGetUnspentUrl : blockchain网站获取正式网络未花费零钱url
	 */
	private String blockchainMainnetGetUnspentUrl;
	/**
	 * bitcoinjNetworkparameters : 网络类型MainNet/TestNet3
	 */
	private String bitcoinjNetworkparameters;
	
	/**
	 * blockchainGetUnspentUrl : 获取未花费零钱url
	 */
	private String blockchainGetUnspentUrl;
	
	/**
	 * blockchainLatestblockUrl : 获取最高区块url地址
	 */
	private String blockchainLatestblockUrl;
	
	/**
	 * blockchainTxinfoUrl : 获取交易详情url地址,需要在后面添加txhash
	 */
	private String blockchainTxinfoUrl;
	
	public void init(){
		log.info("初始化");
		blockchainTestnetGetUnspentUrl = sysCoinfigClient.getSysConfigValue("blockchain_testnetGetUnspent_url");
		blockchainMainnetGetUnspentUrl = sysCoinfigClient.getSysConfigValue("blockchain_mainnetGetUnspent_url");
		bitcoinjNetworkparameters = sysCoinfigClient.getSysConfigValue("bitcoinj_networkparameters");
		blockchainLatestblockUrl = sysCoinfigClient.getSysConfigValue("blockchain_latestblock_url");
		blockchainTxinfoUrl = sysCoinfigClient.getSysConfigValue("blockchain_txinfo_url");
		
		if(bitcoinjNetworkparameters.equals("MainNet")){
			blockchainGetUnspentUrl = blockchainMainnetGetUnspentUrl;
		}else{
			blockchainGetUnspentUrl = blockchainTestnetGetUnspentUrl;
		}
		log.info("初始化,结束");
	}
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method getUnspent
	 * @param address
	 * @param txid
	 * @param index
	 * @throws Throwable 
	 * @return UTXO
	 * @date 2018年9月4日 下午7:58:28
	 */
	public UTXO getUnspent(String address,String txid,long index) throws Throwable {
		log.info("blockchain查找零钱");
		try{
			ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(blockchainGetUnspentUrl + address, JSONObject.class);
			
			JSONObject jsonObject = responseEntity.getBody();
			JSONArray jsonArray = jsonObject.getJSONArray("unspent_outputs");
			for(int i = 0 ; i < jsonArray.size() ; i++){
				JSONObject utxoJson = jsonArray.getJSONObject(i);
				if(utxoJson.getString("tx_hash_big_endian").equals(txid) && utxoJson.getString("tx_output_n").equals(String.valueOf(index))){
					String value = utxoJson.getString("value");
					value = new BigDecimal(value).divide(new BigDecimal("100000000"), 8, BigDecimal.ROUND_HALF_UP).toPlainString();
					UTXO utxo = new UTXO.Builder()//
						.setTxid(txid)//
						.setIndex(String.valueOf(index))//
						.setConfirmations(utxoJson.getString("confirmations"))//
						.setScriptPubKey(utxoJson.getString("script"))//
						.setValue(value)//
						.setAddress(address)
						.build();
					return utxo;
				}
			}
			throw new Exception("未找到");
		}catch(Throwable e){
			log.error("blockchain查找零钱失败",e);
			throw e;
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 访问blockchain 获取一个地址未使用的零钱
	 * @method getUTXOs
	 * @param addressStr
	 * @return Optional<List<UTXO>>
	 * @date 2018年9月4日 下午8:04:55
	 */
	public Optional<List<UTXO>> getUTXOs(String address){
		Optional.ofNullable(address).orElseThrow(() -> new IllegalArgumentException());
		ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(blockchainGetUnspentUrl + address, JSONObject.class);
		Optional<JSONArray> unspentOutputs = Optional.ofNullable(responseEntity)
			.map(p -> p.getBody())
			.map(p -> p.getJSONArray("unspent_outputs"));
		
		ArrayList<UTXO> returnList = new ArrayList<UTXO>();
		for(int i = 0 ; i < unspentOutputs.orElseGet(() -> new JSONArray()).size() ; i++){
			JSONArray jsonArray = unspentOutputs.orElseThrow(() -> new RuntimeException());
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			
			UTXO utxo = new UTXO.Builder()
				.setConfirmations(jsonObject.getString("confirmations"))
				.setIndex(jsonObject.getString("tx_output_n"))
				.setScriptPubKey(jsonObject.getString("script"))
				.setTxid(jsonObject.getString("tx_hash_big_endian"))
				.setValue(
						new BigDecimal(jsonObject.getString("value"))
						.divide(new BigDecimal("100000000"), 8, BigDecimal.ROUND_HALF_UP)
						.toPlainString())
				.setAddress(address)
				.build();
			returnList.add(utxo);
		}
		return Optional.ofNullable(returnList);
	}
	
	/**
	 * @author WangWei
	 * @Description 获取区块最高高度
	 * @method getBlockHeight
	 * @return Optional<Long>
	 * @date 2018年9月7日 上午11:37:48
	 */
	public Optional<Long> getBlockHeight(){
		ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(blockchainLatestblockUrl, JSONObject.class);
		return Optional.ofNullable(responseEntity)
			.map(p -> p.getBody())
			.map(p -> p.getLong("height"));
	}
	
	/**
	 * @author WangWei
	 * @Description 返回交易确认数,当未找到时返回0
	 * @method getConfirmationsBtTxHash
	 * @param txHash
	 * @return Optional<Long>
	 * @date 2018年9月7日 上午11:27:27
	 */
	public Optional<Long> getConfirmationsBtTxHash(String txHash){
		Optional.ofNullable(txHash).orElseThrow(() -> new IllegalArgumentException());
		Long blockHeight = getBlockHeight().orElseThrow(() -> new RuntimeException("blockChain查找确认区快数失败"));
		ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(blockchainTxinfoUrl + txHash, JSONObject.class);
		Optional<Long> importBlock = Optional.ofNullable(responseEntity)
			.map(p -> p.getBody())
			.map(p -> p.getLong("block_height"));
		if(!importBlock.isPresent()){
			return Optional.ofNullable(0L);
		}else{
			Long importBlockLong = importBlock.orElseThrow(() -> new RuntimeException("blockChain查找确认区快数失败"));
			return Optional.ofNullable(blockHeight - importBlockLong + 1L);
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 获取交易确认高度
	 * @method getBlockHeightByTxhash
	 * @param txhash
	 * @return Optional<Long>
	 * @date 2018年9月14日 下午4:17:01
	 */
	public Optional<Long> getBlockHeightByTxhash(String txhash){
		Optional.ofNullable(txhash).orElseThrow(() -> new IllegalArgumentException());
		ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(blockchainTxinfoUrl + txhash, JSONObject.class);
		Optional<Long> importBlock = Optional.ofNullable(responseEntity)
			.map(p -> p.getBody())
			.map(p -> p.getLong("block_height"));
		return importBlock;
	}
	
}
