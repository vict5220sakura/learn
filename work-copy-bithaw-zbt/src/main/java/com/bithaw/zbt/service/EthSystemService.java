package com.bithaw.zbt.service;

/**
 * @Description eth广播操作,根据数据库广播操作
 * @author   WangWei
 * @date     2018年8月24日 上午11:35:05
 * @version  V 1.0
 */
public interface EthSystemService {
	/**
	 * @author WangWei
	 * @Description 初始化
	 * @method init 
	 * @return void
	 * @date 2018年8月24日 上午11:35:03
	 */
	void init();
	
	/**
	 * @author WangWei
	 * @Description 检测节点状态
	 * @method checkLocalNode
	 * @return 
	 * @date 2018年9月11日 下午4:24:39
	 */
	public void checkLocalNode();

	/**
	 * @author WangWei
	 * @Description 查询交易发送后的状态
	 * @method getState
	 * @param orderNo
	 * @return -1:不存在;0:打包中;1:交易成功;-2:交易被覆盖
	 * @return int
	 * @date 2018年8月24日 上午11:36:42
	 */
	public int getState(String orderNo);
	
	/**
	 * @author WangWei
	 * @Description 设置nonce定时任务
	 * @method setNonceTask 
	 * @return void
	 * @date 2018年9月10日 上午10:43:47
	 */
	void setNonceTask();
	
	/**
	 * @author WangWei
	 * @Description 设置nonce异步执行
	 * @method setNonceAsync 
	 * @return void
	 * @date 2018年9月10日 上午10:44:44
	 */
	void setNonceAsync();

	
	/**
	 * @author WangWei
	 * @Description 设置一个地址的nonce
	 * @method setNonce
	 * @param fromAddress 
	 * @return void
	 * @date 2018年9月10日 上午11:50:22
	 */
	public void setNonce(String fromAddress);
	
	/**
	 * @author WangWei
	 * @Description 查找state = 3 的交易,根据时间排序,根据数据库本地私钥进行签名,将签名后的数据写回数据库,改state = 4
	 * @method localSignTask 
	 * @return void
	 * @date 2018年9月10日 下午2:10:37
	 */
	public void localSignTask();
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method localSignAsync 
	 * @return void
	 * @date 2018年9月10日 下午2:30:27
	 */
	void localSignAsync();

	/**
	 * @author WangWei 
	 * @Description 定时查找发送交易 定时查找状态0|4的交易并发送
	 * @method 
	 * @return void
	 * @date 2018年8月24日 上午11:37:47
	 */
	public void sendTask();
	
	/**
	 * @author WangWei 
	 * @Description 定时查找发送交易 定时查找状态0的交易并发送
	 * @method 
	 * @return void
	 * @date 2018年8月24日 上午11:37:47
	 */
	public void sendAsync();
	
	/**
	 * @author WangWei
	 * @Description 扫描状态2,4的交易并确认是否有6个区块确认,可能发生的情况是最终状态为1或5
	 * @method ensureTxByEtherscanTask 
	 * @return void
	 * @date 2018年8月24日 上午11:39:55
	 */
	public void ensureTxByEtherscanTask();//目前只做etherscan的
	
	/**
	 * @author WangWei
	 * @Description 定时查找没有txhash的交易并补全,目前只有第三方etherscan实现
	 * @method scanAndEnsureTxhash 
	 * @return void
	 * @date 2018年8月24日 上午11:40:28
	 */
	public void etherscanRepairTxhashTask();
}
