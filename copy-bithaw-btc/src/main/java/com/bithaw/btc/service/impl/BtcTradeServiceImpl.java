/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月14日  上午11:26:54
 * @version   V 1.0
 */
package com.bithaw.btc.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bithaw.btc.entity.BtcTrade;
import com.bithaw.btc.feign.SysCoinfigClient;
import com.bithaw.btc.mapper.BtcTradeMapper;
import com.bithaw.btc.service.BtcTradeService;
import com.bithaw.btc.utils.BitcoinjUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月14日 上午11:26:54
 * @version  V 1.0
 */
@Slf4j
@Service
public class BtcTradeServiceImpl implements BtcTradeService{
	@Autowired
	private BitcoinjUtil bitcoinjUtil;
	@Autowired
	private SysCoinfigClient sysCoinfigClient;
	@Autowired
	private BtcTradeMapper btcTradeMapper;
	/** 
	 * <p>Title: trade</p>
	 * <p>Description: 转账直接存储进数据库</p>
	 * @param toAddress
	 * @param amount
	 * @param uuid
	 * @return String SUCCESS:成功;FAIL:失败
	 * @see com.bithaw.btc.service.BtcSystemService#trade(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public String trade(String toAddress, String amount, String uuid,String fees) {
		log.info("比特币提币开始");
		
		if("0".equals(fees)){
			fees = sysCoinfigClient.getSysConfigValue("btc_fees");
		}
		
		boolean checkAddress = bitcoinjUtil.checkAddress(toAddress);
		if(!checkAddress){
			log.info("转账地址错误,转账失败");
			return "FAIL";
		}
		
		BtcTrade bean = btcTradeMapper.findByUuid(uuid);
		if(bean != null){
			return "SUCCESS";
		}
		
		BtcTrade btcTrade = new BtcTrade().builder()
			.amount(new BigDecimal(amount))
			.createTime(new Date())
			.fees( new BigDecimal( fees) )
			.state(0)
			.toAddress(toAddress)
			.uuid(uuid)
			.build();
		btcTradeMapper.save(btcTrade);
		log.info("比特币提币完成");
		return "SUCCESS";
	}
}
