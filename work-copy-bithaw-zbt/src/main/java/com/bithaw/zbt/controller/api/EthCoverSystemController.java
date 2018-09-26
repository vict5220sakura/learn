/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月13日  下午6:56:46
 * @version   V 1.0
 */
package com.bithaw.zbt.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bithaw.common.annotation.RpcAuthRequire;
import com.bithaw.zbt.service.EthCoverSystemService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月13日 下午6:56:46
 * @version  V 1.0
 */
@Slf4j
@RestController
public class EthCoverSystemController {
	@Autowired
	private EthCoverSystemService ethCoverSystemService;
	
	/**
	 * @author WangWei
	 * @Description 根据orderNo查询交易状态,查询数据库
	 * @method getState
	 * @param coverNo
	 * @return String
	 * @date 2018年9月10日 下午4:42:17
	 */
	@PostMapping(value = "/eth/cover/getState")
	public String getState(String coverNo){
		log.info("据coverNo查询交易状态:开始");
		int state = ethCoverSystemService.getState(coverNo);
		if(state == -1){
			log.info("据orderNo查询交易状态:结束,coverNo{},state{}",coverNo,"失败");
			return "FAIL";
		}
		if(state == 0){
			log.info("据orderNo查询交易状态:结束,coverNo{},state{}",coverNo,"打包中");
			return "PENDING";
		}
		if(state == 1){
			log.info("据orderNo查询交易状态:结束,coverNo{},state{}",coverNo,"成功");
			return "SUCCESS";
		}
		if(state == -2){
			log.info("据orderNo查询交易状态:结束,coverNo{},state{}",coverNo,"被覆盖");
			return "OVERRIDE";
		}
		log.info("据orderNo查询交易状态:结束,coverNo{},state{}",coverNo,"查询失败返回失败");
		return "FAIL";
	}
	
	/**
	 * @author WangWei
	 * @Description 覆盖交易定时设置nonce任务,有分布式锁
	 * @method setNonceTask 
	 * @return void
	 * @date 2018年9月10日 下午4:37:34
	 */
	@PostMapping(value = "/eth/cover/setNonceTask")
	public void setNonceTask(){
		log.info("覆盖交易设置nonce任务开始");
		ethCoverSystemService.setNonceTask();
	}
	
	/**
	 * @author WangWei
	 * @Description 覆盖交易本地签名任务,有分布式锁
	 * @method localSignTask 
	 * @return void
	 * @date 2018年9月10日 下午4:38:47
	 */
	@PostMapping(value = "/eth/cover/localSignTask")
	public void localSignTask(){
		log.info("覆盖交易本地签名任务开始");
		ethCoverSystemService.localSignTask();
	}
	
	/**  
	 * @author: WangWei 
	 * @date: 2018年8月24日 上午10:00:55 
	 * @version: v1.0.0
	 * @Description:  扫描交易发送到以太坊公链,有分布式锁
	 */
	@PostMapping(value = "/eth/cover/sendTask")
	public void sendTask(){
		log.info("覆盖交易广播 开始");
		ethCoverSystemService.sendTask();
	}
	
	/**
	 * @author WangWei
	 * @Description 覆盖交易,补全txhash任务,没有分布式锁
	 * @method scanAndEnsureTxhash 
	 * @return void
	 * @date 2018年9月10日 下午4:42:00
	 */
	@PostMapping(value = "/eth/cover/etherscanRepairTxhashTask")
	public void etherscanRepairTxhashTask(){
		log.info("覆盖交易查到没有txhash的交易,并根据交易信息上etherscan查找信息补全txhash:开始");
		ethCoverSystemService.etherscanRepairTxhashTask();
	}

	/**
	 * @author WangWei
	 * @Description 覆盖交易确认交易任务,没有分布式锁
	 * @method scanAndEnsureStateNot1 
	 * @return void
	 * @date 2018年9月10日 下午4:41:24
	 */
	@PostMapping(value = "/eth/cover/etherscanEnsureTxTask")
	public void etherscanEnsureTxTask(){
		log.info("覆盖交易确认交易 开始");
		ethCoverSystemService.ensureTxByEtherscanTask();
	}
	
	/**
	 * @author WangWei
	 * @Description 覆盖交易,最终确认任务
	 * @method scanAndEnsureTxhash 
	 * @return void
	 * @date 2018年9月10日 下午4:42:00
	 */
	@PostMapping(value = "/eth/cover/finalStateTask")
	public void finalStateTask(){
		log.info("覆盖交易最终确认");
		ethCoverSystemService.finalStateTask();
	}
	
}
