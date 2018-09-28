/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月7日  下午4:50:31
 * @version   V 1.0
 */
package com.bithaw.btc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bithaw.btc.entity.BtcTrade;
import com.bithaw.btc.entity.BtcTradeInput;
import com.bithaw.btc.entity.BtcTradeOutput;
import com.bithaw.btc.mapper.BtcTradeInputMapper;
import com.bithaw.btc.mapper.BtcTradeMapper;
import com.bithaw.btc.mapper.BtcTradeOutputMapper;
import com.bithaw.btc.service.BtcSignService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月7日 下午4:50:31
 * @version  V 1.0
 */
@Slf4j
@Service
public class BtcSignServiceImpl implements BtcSignService {

	@Autowired
	private BtcTradeMapper btcTradeMapper;
	@Autowired
	private BtcTradeInputMapper btcTradeInputMapper;
	@Autowired
	private BtcTradeOutputMapper btcTradeOutputMapper;
	
	/** 
	 * <p>Title: getAwaitData</p>
	 * <p>Description: 查找待签名的数据 state=3</p>
	 * @return
	 * @see com.bithaw.btc.service.BtcSignService#getAwaitData()  
	 */
	@Override
	public JSONObject getAwaitData() {
		log.info("btc获取构建成功待签名数据");
		JSONObject returnJsonObject = new JSONObject();
		JSONArray dataJSONArray = new JSONArray();
		List<BtcTrade> BtcTrades = btcTradeMapper.findByState(3);
		for(BtcTrade btcTrade : BtcTrades){
			JSONObject btcTradeJson = getAwaitData(btcTrade);
			dataJSONArray.add(btcTradeJson);
		}
		returnJsonObject.put("data", dataJSONArray);
		return returnJsonObject;
	}

	/**
	 * @author WangWei
	 * @Description 获取一个交易的json数据
	 * @method getAwaitData
	 * @param btcTrade
	 * @return JSONObject
	 * @date 2018年9月7日 下午5:03:04
	 */
	private JSONObject getAwaitData(BtcTrade btcTrade) {
		JSONObject returnJsonObject = new JSONObject();
		
		List<BtcTradeInput> BtcTradeInputs = btcTradeInputMapper.findByUuid(btcTrade.getUuid());
		List<BtcTradeOutput> BtcTradeOutputs = btcTradeOutputMapper.findByUuid(btcTrade.getUuid());
		JSONArray inputArray = getInputArray(BtcTradeInputs);
		JSONArray outputArray = getOutputArray(BtcTradeOutputs);
		
		returnJsonObject.put("uuid", btcTrade.getUuid());
		returnJsonObject.put("toAddress", btcTrade.getToAddress());
		returnJsonObject.put("amount", btcTrade.getAmount().toPlainString());
		returnJsonObject.put("fees", btcTrade.getFees().toPlainString());
		returnJsonObject.put("inputs", inputArray);
		returnJsonObject.put("outputs", outputArray);
		
		return returnJsonObject;
	}

	/**
	 * @author WangWei
	 * @Description 获取input 格式化的JSON字符串
	 * @method getInputArray
	 * @param btcTradeInputs
	 * @return JSONArray
	 * @date 2018年9月7日 下午5:10:36
	 */
	private JSONArray getInputArray(List<BtcTradeInput> btcTradeInputs) {
		JSONArray returnJSONArray = new JSONArray();
		for(BtcTradeInput btcTradeInput : btcTradeInputs){
			JSONObject inputJson = getInputJSON(btcTradeInput);
			returnJSONArray.add(inputJson);
		}
		return returnJSONArray;
	}
	
	/**
	 * @author WangWei
	 * @Description 转换input到格式化json
	 * @method getInputJSON
	 * @param btcTradeInput
	 * @return JSONObject
	 * @date 2018年9月7日 下午5:13:00
	 */
	private JSONObject getInputJSON(BtcTradeInput btcTradeInput) {
		JSONObject returnJson = new JSONObject();
		
		returnJson.put("txId", btcTradeInput.getTxId());
		returnJson.put("txIndex", String.valueOf(btcTradeInput.getTxIndex()));
		returnJson.put("amount", btcTradeInput.getAmount().toPlainString());
		returnJson.put("address", btcTradeInput.getAddress());
		returnJson.put("scriptPubkey", btcTradeInput.getScriptPubkey());
		
		return returnJson;
	}

	/**
	 * @author WangWei
	 * @Description 获取output 格式化的JSON字符串
	 * @method getOutputArray
	 * @param btcTradeOutputs
	 * @return JSONArray
	 * @date 2018年9月7日 下午5:10:38
	 */
	private JSONArray getOutputArray(List<BtcTradeOutput> btcTradeOutputs) {
		JSONArray returnJSONArray = new JSONArray();
		for(BtcTradeOutput btcTradeOutput : btcTradeOutputs){
			JSONObject inputJson = getOutputJSON(btcTradeOutput);
			returnJSONArray.add(inputJson);
		}
		return returnJSONArray;
	}

	/**
	 * @author WangWei
	 * @Description 转换output到格式化json
	 * @method getOutputJSON
	 * @param btcTradeOutput
	 * @return JSONObject
	 * @date 2018年9月7日 下午5:13:36
	 */
	private JSONObject getOutputJSON(BtcTradeOutput btcTradeOutput) {
		JSONObject returnJson = new JSONObject();
		
		returnJson.put("address", btcTradeOutput.getAddress());
		returnJson.put("amount", btcTradeOutput.getAmount().toPlainString());
		
		return returnJson;
	}

	/** 
	 * <p>Title: setRawTransaction</p>
	 * <p>Description: </p>
	 * @param uuid
	 * @param rawTransaction
	 * @see com.bithaw.btc.service.BtcSignService#setRawTransaction(java.lang.String, java.lang.String)  
	 */
	@Override
	public void setRawTransaction(String uuid, String rawTransaction) {
		log.info("设置btc签名");
		BtcTrade btcTrade = btcTradeMapper.findByUuid(uuid);
		btcTrade.setRawTransaction(rawTransaction);
		btcTrade.setState(4);
		btcTradeMapper.save(btcTrade);
	}
}
