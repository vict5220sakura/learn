package com.bithaw.btc.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bithaw.btc.feign.SysCoinfigClient;
import com.bithaw.btc.utils.BitRpcUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(value = 1)
public class BitRpcUtilsInit implements ApplicationRunner{

	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	@Autowired
	private BitRpcUtils bitRpcUtils;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		log.info("初始化BitPrcUtils");
		bitRpcUtils.init(//初始化BitPrcUtils
				sysCoinfigClient.getSysConfigValue("btc_rpc_ip")//
				, sysCoinfigClient.getSysConfigValue("btc_rpc_port")//
				, sysCoinfigClient.getSysConfigValue("btc_rpc_user")//
				, sysCoinfigClient.getSysConfigValue("btc_rpc_password")//
				, sysCoinfigClient.getSysConfigValue("bitcoinj_networkparameters"));
		log.info("初始化BitPrcUtils完毕");
	}

}
