package com.bithaw.zbt.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

/**
 * @ @author
 */
//@Component
public class EthTransactionListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(EthTransactionListener.class);

	@Autowired
	private Web3j web3j;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent evt) {

		// if (evt.getApplicationContext().getParent() != null) {
		// return;
		// }

		// 监听用户转入的交易记录 DefaultBlockParameterName.LATEST
		web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameterName.LATEST).subscribe(tx -> {

			// 这个可以根据用户的平台钱包获取转入记录
			//log.info("Transaction:{}", tx.getHash());
		});

	}
}
