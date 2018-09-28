/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月7日  下午4:16:46
 * @version   V 1.0
 */
package com.bithaw.btc.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description 比特币离线签名service
 * @author   WangWei
 * @date     2018年9月7日 下午4:16:46
 * @version  V 1.0
 */
public interface BtcSignService {
	/**
	 * @author WangWei
	 * @Description 获取待签名数据
	 * @method getAwaitData
	 * @return JSONObject 
	 * 数据结构{"data": [{
	 *            "uuid": "",
	 *            "toAddress": "",
	 *            "amount": "",
	 *            "fees": "",
	 *            "inputs": [
	 *                {
	 *                    "txId": "",
	 *                    "txIndex": "",
	 *                    "amount": "",
	 *                    "address": "",
	 *                    "scriptPubkey": ""
	 *                }],
	 *            "outputs": [
	 *                {
	 *                    "address": "",
	 *                    "amount": ""
	 *                }
	 *            ]
	 *        }
	 *    ],
	 *    "sign": ""}
	 * @date 2018年9月7日 下午4:19:56
	 */
	public JSONObject getAwaitData();

	/**
	 * @author WangWei
	 * @Description 设置签名后的数据
	 * @method setRawTransaction
	 * @param uuid
	 * @param rawTransaction 
	 * @return void
	 * @date 2018年9月7日 下午5:45:21
	 */
	public void setRawTransaction(String uuid, String rawTransaction);
}
