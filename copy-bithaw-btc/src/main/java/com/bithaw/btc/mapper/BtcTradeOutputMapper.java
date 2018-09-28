/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午4:24:06
 * @version   V 1.0
 */
package com.bithaw.btc.mapper;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bithaw.btc.entity.BtcTradeOutput;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午4:24:06
 * @version  V 1.0
 */
public interface BtcTradeOutputMapper extends JpaRepository<BtcTradeOutput,BigInteger>{
	public List<BtcTradeOutput> findByUuid(String uuid);
}
