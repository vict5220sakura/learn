/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月19日  下午1:46:07
 * @version   V 1.0
 */
package com.start.dao;


import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.start.bean.Money;


/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月19日 下午1:46:07
 * @version  V 1.0
 */
public interface MoneyDao extends JpaRepository<Money,Integer>{
	@Transactional
	@Modifying
	@Query(value = "update test_line_lock set money = money - ?2 where id = ?1 and money - ?2 >= 0;",nativeQuery = true)
	public int pay(Integer id,BigDecimal money);
}
