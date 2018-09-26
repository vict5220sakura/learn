/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月27日  下午3:23:03
 * @version   V 1.0
 */
package com.bithaw.zbt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bithaw.zbt.entity.EthTradeNonce;
import com.bithaw.zbt.mapper.EthTradeNonceMapper;
import com.bithaw.zbt.service.SignService;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月27日 下午3:23:03
 * @version  V 1.0
 */
@Service
public class SignServiceImpl implements SignService {

	@Autowired
	private EthTradeNonceMapper ethTradeNonceMapper;
	
	
	/** 
	 * <p>Title: getAwaitData</p>
	 * <p>Description: 返回类型 jsonobject,data:[{},,],sign:"data参数字符串"</p>
	 * @return
	 * @see com.bithaw.zbt.service.SignService#getAwaitData()  
	 * BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data
	 */
	@Override
	public JSONObject getAwaitData() {
		JSONObject returnJson = new JSONObject();
		returnJson.put("data", new JSONArray());
		List<EthTradeNonce> findAllRawTranssactionNull = ethTradeNonceMapper.findAllRawTranssactionNull();
		for(EthTradeNonce ethTradeNonce : findAllRawTranssactionNull){
			JSONObject jsonObjectTemp = new JSONObject();
			jsonObjectTemp.put("from", ethTradeNonce.getFromAddress());
			jsonObjectTemp.put("orderNo", ethTradeNonce.getOrderNo());
			jsonObjectTemp.put("nonce", ethTradeNonce.getNonce() + "");
			jsonObjectTemp.put("gasPrice", ethTradeNonce.getGasPrice().toPlainString());
			jsonObjectTemp.put("gasLimit", ethTradeNonce.getGasLimit() + "");
			jsonObjectTemp.put("to", ethTradeNonce.getToAddress());
			jsonObjectTemp.put("value", ethTradeNonce.getValue().toPlainString());
			jsonObjectTemp.put("data", ethTradeNonce.getData());
			returnJson.getJSONArray("data").add(jsonObjectTemp);
		}
		return returnJson;
	}

	/** 
	 * <p>Title: setRawTransaction</p>
	 * <p>Description: </p>
	 * @param orderNo
	 * @param rawTransaction
	 * @see com.bithaw.zbt.service.SignService#setRawTransaction(java.lang.String, java.lang.String)  
	 */
	@Override
	public void setRawTransaction(String orderNo, String rawTransaction) {
		EthTradeNonce findByorderNo = ethTradeNonceMapper.findByorderNo(orderNo);
		findByorderNo.setRawTransaction(rawTransaction);
		ethTradeNonceMapper.save(findByorderNo);
	}
}
