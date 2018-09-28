/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午2:38:34
 * @version   V 1.0
 */
package com.bithaw.btc.mapper;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bithaw.btc.entity.BtcTrade;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午2:38:34
 * @version  V 1.0
 */
public interface BtcTradeMapper extends JpaRepository<BtcTrade,BigInteger>{
	/**
	 * @author WangWei
	 * @Description 根据状态查找
	 * @method findByState
	 * @param state
	 * @return List<BtcTrade>
	 * @date 2018年9月6日 上午11:44:49
	 */
	List<BtcTrade> findByState(int state);
	
	BtcTrade findByUuid(String uuid);
}
