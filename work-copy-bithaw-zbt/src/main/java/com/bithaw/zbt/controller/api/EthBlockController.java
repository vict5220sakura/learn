package com.bithaw.zbt.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bithaw.common.annotation.RpcAuthRequire;
import com.bithaw.zbt.service.EthBlockService;

import lombok.extern.slf4j.Slf4j;

/**
 * 以太坊扫描区块同步类
 * @author WangWei
 * @date: 2018年8月24日 上午9:46:42
 * @version: v1.0.0
 * @Description:
 */
@Slf4j
@RestController
public class EthBlockController {
	
	@Autowired
	private EthBlockService ethBlockService;
	
	/**
	 * @author WangWei
	 * @Description 初始化方法
	 * @method init 
	 * @return void
	 * @date 2018年9月11日 下午5:26:41
	 */
	public void init(){
		log.info("初始化 开始");
	}
	
	/**
	 * @author WangWei
	 * @Description 本地节点扫描区块任务
	 * @method synchronization 
	 * @return void
	 * @date 2018年9月11日 下午5:26:57
	 */
	@PostMapping("/eth/block/scanBlockLocalTask")
	public void scanBlockLocalTask() {
		ethBlockService.scanBlockLocalTask();
	}
	
	/**
	 * @author WangWei
	 * @Description Etherscan同步数据接口
	 * @method synchronizationEtherscan
	 * @param  
	 * @return void
	 * @date 2018年8月24日 上午11:01:38
	 */
	@PostMapping("/eth/block/scanBlockEtherscanTask")
	public void scanBlockEtherscanTask(){
		ethBlockService.scanBlockEtherscanTask();
	}
	
}
