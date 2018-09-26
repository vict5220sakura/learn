/**
 * @Description  签名服务类
 * @author  WangWei
 * @Date    2018年8月27日  下午3:17:03
 * @version   V 1.0
 */
package com.bithaw.zbt.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description 签名服务接口
 * @author   WangWei
 * @date     2018年8月27日 下午3:17:03
 * @version  V 1.0
 */
public interface SignService {
	
	/**
	 * @author WangWei
	 * @Description 获取待签名的数据
	 * @method getAwaitData
	 * @return 
	 * @return JSONObject
	 * @date 2018年8月27日 下午3:22:25
	 */
	public JSONObject getAwaitData();
	
	/**
	 * @author WangWei
	 * @Description 设置签名数据
	 * @method setRawTransaction
	 * @param orderNo
	 * @param rawTransaction 
	 * @return void
	 * @date 2018年8月27日 下午5:57:38
	 */
	public void setRawTransaction(String orderNo,String rawTransaction);
}
