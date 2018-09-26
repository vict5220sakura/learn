/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月19日  下午2:02:12
 * @version   V 1.0
 */
package com.start.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.start.dao.MoneyDao;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月19日 下午2:02:12
 * @version  V 1.0
 */
@Service
public class MoneyService {
	
	@Autowired
	private MoneyDao moneyDao;
	
	public void pay(int id){
		int pay = moneyDao.pay(id,new BigDecimal("10"));
		System.out.println(pay);
	}
}
