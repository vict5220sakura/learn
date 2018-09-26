package com.bithaw.zbt.service;

/**
 * @Description 区块映射到数据库操作
 * @author   WangWei
 * @date     2018年8月24日 上午11:28:45
 * @version  V 1.0
 */
public interface EthBlockService {
	
	/**
	 * @author WangWei
	 * @Description 初始化
	 * @method init
	 * @param  
	 * @return void
	 * @date 2018年8月24日 上午11:29:31
	 */
	public void init();
	
	/**
	 * @author WangWei
	 * @Description 
	 * @method scanBlockLocalTask 
	 * @return void
	 * @date 2018年9月11日 下午5:32:04
	 */
	public void scanBlockLocalTask();

	/**
	 * @author WangWei
	 * @Description 多线程执行扫描本地节点区块,同步信息任务
	 * @method scanBlockLocalAynsc
	 * @param ethScanBlockHeightThreadCount
	 * @param i 
	 * @return void
	 * @date 2018年9月11日 下午5:38:51
	 */
	public void scanBlockLocalAsync(int ethScanBlockHeightThreadCount, int i);

	/**
	 * @author WangWei
	 * @Description Etherscan同步数据接口
	 * @method scanBlockEtherscanTask 
	 * @return void
	 * @date 2018年9月11日 下午6:13:40
	 */
	public void scanBlockEtherscanTask();
	
	/**
	 * @author WangWei
	 * @Description Etherscan同步数据接口异步
	 * @method scanBlockEtherscanAsync 
	 * @return void
	 * @date 2018年9月11日 下午6:16:12
	 */
	public void scanBlockEtherscanAsync();
}
