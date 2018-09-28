/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午3:46:24
 * @version   V 1.0
 */
package com.bithaw.btc.mapper;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bithaw.btc.entity.BtcTradeInput;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午3:46:24
 * @version  V 1.0
 */
public interface BtcTradeInputMapper extends JpaRepository<BtcTradeInput,BigInteger>{

	/**
	 * @author WangWei
	 * @Description 查找零钱是否存在
	 * @method findByTxIdAndTxIndex
	 * @param txId 零钱hash
	 * @param txIndex 零钱index
	 * @return BtcTradeInput
	 * @date 2018年9月6日 上午11:02:31
	 */
	public BtcTradeInput findByTxIdAndTxIndex(String txId,int txIndex);
	
	public List<BtcTradeInput> findByUuid(String uuid);
}
