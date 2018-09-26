/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午1:52:31
 * @version   V 1.0
 */
package com.bithaw.zbt.service;

import java.math.BigDecimal;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月12日 下午1:52:31
 * @version  V 1.0
 */
public interface EthTradeService {

	/**
	 * @author WangWei
	 * @Description 初始化
	 * @method init void
	 * @date 2018年9月12日 下午2:06:18
	 */
	public void init();
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method newAccount
	 * @param password
	 * @return String
	 * @date 2018年9月12日 下午2:09:46
	 */
	String newAccount(String password);
	
	/**
	 * @author WangWei
	 * @Description zbt转账
	 * @method zbtTrade
	 * @param orderNo
	 * @param toAddress
	 * @param amount 
	 * @return void
	 * @date 2018年9月10日 上午9:36:37
	 */
	public String zbtTrade(String orderNo,String fromAddress,  String toAddress, String amount);
	
	/**
	 * @author WangWei
	 * @Description 查询冻结状态
	 * @method selectFreeze
	 * @param address
	 * @return String true:冻结;false:未冻结
	 * @date 2018年9月12日 下午4:15:19
	 */
	public String selectFreeze(String address);
	
	/**
	 * @author WangWei
	 * @Description 冻结
	 * @method zbtAccountFreeze
	 * @param orderNo
	 * @param fromAddress 管理员地址
	 * @param address 冻结地址
	 * @return String
	 * @date 2018年9月12日 下午3:50:13
	 */
	public String zbtAccountFreeze(String orderNo,String fromAddress,String address);

	/**
	 * @author WangWei
	 * @Description 解冻
	 * @method zbtAccountUnfreeze
	 * @param orderNo
	 * @param fromAddress 管理员地址
	 * @param address 解冻地址
	 * @return String
	 * @date 2018年9月12日 下午4:31:00
	 */
	public String zbtAccountUnfreeze(String orderNo, String fromAddress, String address);

	/**
	 * @author WangWei
	 * @Description eth通用覆盖交易,指定orderNo
	 * @method ethCoverTransaction
	 * @param coverNo 订单号 
	 * @param orderNo 原交易订单号 
	 * @param gasPriceStr (gwei)
	 * @param toAddress 覆盖后的内容
	 * @param value 覆盖后的内容
	 * @param data 覆盖的data数据
	 * @date 2018年9月12日 下午7:16:11
	 */
	boolean ethCoverTransaction(String coverNo, String orderNo, String gasPriceStr, String toAddress, BigDecimal value, String data);

	/**
	 * @author WangWei
	 * @Description 扎比特交易覆盖
	 * @method zbtTradeCover
	 * @param coverNo 覆盖交易号
	 * @param orderNo 原订单号
	 * @param gasPriceStr 覆盖交易的手续费
	 * @param toAddress 扎特币转账的目的地地址
	 * @param amount 金额
	 * @return String SUCCESS:发送成功;FAIL:发送失败,原交易已经成功;ERROR:系统错误导致发送失败
	 * @date 2018年9月12日 下午7:30:05
	 */
	String zbtTradeCover(String coverNo, String orderNo, String gasPriceStr, String toAddress, String amount);


}
