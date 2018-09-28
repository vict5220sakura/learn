/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月14日  上午11:26:44
 * @version   V 1.0
 */
package com.bithaw.btc.service;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月14日 上午11:26:44
 * @version  V 1.0
 */
public interface BtcTradeService {
	/**
	 * @author WangWei
	 * @Description 比特币提币
	 * @method trade
	 * @param toAddress
	 * @param amount
	 * @param uuid
	 * @return String SUCCESS:成功;FAIL:失败
	 * @date 2018年9月5日 下午5:41:41
	 */
	public String trade(String toAddress,String amount,String uuid,String fees);
}
