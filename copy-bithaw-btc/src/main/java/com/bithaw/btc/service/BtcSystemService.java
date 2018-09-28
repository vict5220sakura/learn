/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午5:28:47
 * @version   V 1.0
 */
package com.bithaw.btc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.bithaw.btc.entity.BtcTrade;
import com.bithaw.btc.entity.UTXO;

/**
 * @Description 比特币交易service
 * @author   WangWei
 * @date     2018年9月5日 下午5:28:47
 * @version  V 1.0
 */
public interface BtcSystemService {
	
	public void checkNode();
	
	/**
	 * @author WangWei
	 * @Description 创建比特币公私钥对
	 * @method createBtcAddress
	 * @return String 私钥|公钥
	 * @date 2018年9月5日 下午5:30:39
	 */
	public String createBtcAddress();

	
	/**
	 * @author WangWei
	 * @Description 构建交易输入输出任务,查找状态为0的交易
	 * @method buildTradeTask 
	 * @return void
	 * @date 2018年9月6日 下午5:57:04
	 */
	public void buildTradeTask();
	
	/**
	 * @author WangWei
	 * @Description 构建交易任务异步执行
	 * @method buildTrade 
	 * @return void
	 * @date 2018年9月6日 上午11:40:40
	 */
	public void buildTradeAsync();

	/**
	 * @author WangWei
	 * @Description 构建输入输出存入数据库
	 * @method buildInOut
	 * @param btcTrade
	 * @param utxos
	 * @param btcCompanyAddresses
	 * @param changeMoney 
	 * @return void
	 * @date 2018年9月6日 下午6:04:52
	 */
	public void saveInOutDatebase(BtcTrade btcTrade, Optional<List<UTXO>> utxos, String btcCompanyAddresses, BigDecimal changeMoney);

	/**
	 * @author WangWei
	 * @Description 签名任务,查找构建好的交易(state=3),最好在构建交易后调用签名任务,再签名任务后调用广播任务
	 * @method signTask 
	 * @return void
	 * @date 2018年9月6日 下午6:28:50
	 */
	public void signTask();

	/**
	 * @author WangWei
	 * @Description 异步执行签名方法
	 * @method sign 
	 * @return void
	 * @date 2018年9月6日 下午6:36:10
	 */
	public void signAsync();
	
	/**
	 * @author WangWei
	 * @Description 广播任务
	 * @method sendRawtransactionTask 
	 * @return void
	 * @date 2018年9月6日 下午7:48:44
	 */
	public void sendRawtransactionTask();
	
	/**
	 * @author WangWei
	 * @Description 异步广播任务
	 * @method sendRawtransactionAsync 
	 * @return void
	 * @date 2018年9月6日 下午7:48:54
	 */
	public void sendRawtransactionAsync();
	
	/**
	 * @author WangWei
	 * @Description 确认交易
	 * @method ensureTransactionTask 
	 * @return void
	 * @date 2018年9月7日 上午9:16:52
	 */
	public void ensureTransactionTask();
	
	/**
	 * @author WangWei
	 * @Description 确认交易异步任务
	 * @method ensureTransactionAsync 
	 * @return void
	 * @date 2018年9月7日 上午9:18:17
	 */
	public void ensureTransactionAsync();

	/**
	 * @author WangWei
	 * @Description 
	 * @method getTradeStatus
	 * @param uuid
	 * @return String
	 * @date 2018年9月14日 下午3:18:46
	 */
	public String getTradeStatus(String uuid);

	/**
	 * @author WangWei
	 * @Description 获取交易详情
	 * @method getTradeInfo
	 * @param uuid 编号
	 * @return String
	 * @date 2018年9月14日 下午3:41:52
	 */
	public String getTradeInfo(String uuid);
}
