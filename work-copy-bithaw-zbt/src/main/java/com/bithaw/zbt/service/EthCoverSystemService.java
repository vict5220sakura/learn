/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月12日  下午5:23:02
 * @version   V 1.0
 */
package com.bithaw.zbt.service;

/**
 * @Description 覆盖交易service
 * @author   WangWei
 * @date     2018年9月12日 下午5:23:02
 * @version  V 1.0
 */
public interface EthCoverSystemService {

	/**
	 * @author WangWei
	 * @Description 初始化
	 * @method init void
	 * @date 2018年9月13日 上午11:43:08
	 */
	void init();

	/**
	 * @author WangWei
	 * @Description 设置nonce任务
	 * @method setNonceTask void
	 * @date 2018年9月12日 下午7:53:13
	 */
	void setNonceTask();

	/**
	 * @author WangWei
	 * @Description 设置nonce任务异步执行
	 * @method setNonceAsync void
	 * @date 2018年9月12日 下午7:54:02
	 */
	void setNonceAsync();

	/**
	 * @author WangWei
	 * @Description 本地签名任务
	 * @method localSignTask void
	 * @date 2018年9月13日 下午1:53:42
	 */
	void localSignTask();
	
	/**
	 * @author WangWei
	 * @Description 本地异步签名任务
	 * @method localSignAsync void
	 * @date 2018年9月13日 下午1:55:58
	 */
	void localSignAsync();
	
	/**
	 * @author WangWei
	 * @Description 定时广播任务
	 * @method sendTask void
	 * @date 2018年9月13日 下午2:06:39
	 */
	void sendTask();
	
	/**
	 * @author WangWei
	 * @Description 定是广播异步执行
	 * @method sendAsnyc void
	 * @date 2018年9月13日 下午2:06:42
	 */
	void sendAsync();
	
	/**
	 * @author WangWei
	 * @Description 异步任务,扫描补全txhash
	 * @method etherscanRepairTxhashTask void
	 * @date 2018年9月13日 下午3:08:22
	 */
	void etherscanRepairTxhashTask();
	
	/**
	 * @author WangWei
	 * @Description 扫描状态2,4的交易并确认是否有6个区块确认,可能发生的情况是最终状态为1或5
	 * @method ensureTxByEtherscanTask void
	 * @date 2018年9月13日 下午3:32:16
	 */
	void ensureTxByEtherscanTask();

	/**
	 * @author WangWei
	 * @Description 最终状态确认任务
	 * @method finalStateTask void
	 * @date 2018年9月13日 下午4:16:03
	 */
	void finalStateTask();

	/**
	 * @author WangWei
	 * @Description 最终状态确认异步方法
	 * @method finalStateAsync void
	 * @date 2018年9月13日 下午4:17:10
	 */
	void finalStateAsync();

	/**
	 * @author WangWei
	 * @Description 
	 * @method getState
	 * @param coverNo
	 * @return int
	 * @date 2018年9月13日 下午7:02:03
	 */
	int getState(String coverNo);
}
