/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月7日  上午10:00:20
 * @version   V 1.0
 */
package com.bithaw.btc.comtroller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bithaw.btc.service.BtcSystemService;
import com.bithaw.common.annotation.RpcAuthRequire;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 交易定时任务接口
 * @author   WangWei
 * @date     2018年9月7日 上午10:00:23
 * @version  V 1.0
 */
@Slf4j
@RestController
public class BtcSystemController {
	@Autowired
	private BtcSystemService btcSystemService;
	
	/**
	 * @author WangWei
	 * @Description 检测节点状态,写入数据库配置
	 * @method checkNode 
	 * @return void
	 * @date 2018年8月28日 上午11:35:33
	 */
	@PostMapping("/btc/system/checkNode")
	public void checkNode(){
		log.info("检查节点链接接口调用");
		btcSystemService.checkNode();
	}

	/**
	 * @author WangWei
	 * @Description 定时构建交易任务,有分布式锁,可以短时间内调用建议15秒调用一次,同时进行了签名,为了快速返回零钱供其他交易使用
	 * @method buildTradeTask 
	 * @return void
	 * @date 2018年9月7日 上午10:04:04
	 */
	@PostMapping("btc/system/buildTrade")
	public void buildTradeTask(){
		log.info("比特币系统构建交易任务");
		btcSystemService.buildTradeTask();
	}
	
	/**
	 * @author WangWei
	 * @Description 定时签名任务,有分布式锁,可以短时间内调用建议15秒调用一次
	 * @method signTask 
	 * @return void
	 * @date 2018年9月7日 上午10:04:14
	 */
	@PostMapping("btc/system/sign")
	public void signTask(){
		log.info("比特币系统本地签名任务");
		btcSystemService.signTask();
	}
	
	/**
	 * @author WangWei
	 * @Description 定时发送交易任务,有分布式锁,可以短时间内调用建议设置15秒调用一次
	 * @method sendRawtransactionTask 
	 * @return void
	 * @date 2018年9月7日 上午10:04:22
	 */
	@PostMapping("btc/system/sendTx")
	public void sendRawtransactionTask(){
		log.info("比特币系统定时广播任务");
		btcSystemService.sendRawtransactionTask();
	}
	
	/**
	 * @author WangWei
	 * @Description 定时确认交易任务,有分布式锁,可以短时间内调用建议设置15秒调用一次
	 * @method ensureTransactionTask 
	 * @return void
	 * @date 2018年9月7日 上午10:04:32
	 */
	@PostMapping("btc/system/ensureTx")
	public void ensureTransactionTask(){
		btcSystemService.ensureTransactionTask();
	}
}
