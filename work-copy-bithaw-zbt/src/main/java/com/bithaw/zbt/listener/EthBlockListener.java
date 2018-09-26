package com.bithaw.zbt.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

/**
 * 
 * @author 
 */

//@Component
public class EthBlockListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(EthBlockListener.class);
	
	@Autowired
    private Web3j web3j;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent evt) {

		if (evt.getApplicationContext().getParent() != null) {
			return;
		}

		web3j.blockObservable(false).subscribe(block -> {
			// 这里就可以根据新增区块来处理转入转出确认数的逻辑了
			log.info("新增区块:{}", block.getBlock().getNumber());
		});
	}
}
