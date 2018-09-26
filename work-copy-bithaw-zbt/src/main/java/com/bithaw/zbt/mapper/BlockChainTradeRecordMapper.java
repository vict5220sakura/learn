package com.bithaw.zbt.mapper;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bithaw.zbt.entity.BlockChainTradeRecord;

/**
 * @Description BlockChainTradeRecord Jpa Dao
 * @author   WangWei
 * @date     2018年8月24日 上午11:13:56
 * @version  V 1.0
 */
public interface BlockChainTradeRecordMapper extends JpaRepository<BlockChainTradeRecord,BigInteger>{
	/**
	 * @author WangWei
	 * @Description 根据TxHash查询实体
	 * @method findByTxHash
	 * @param @param txHash
	 * @param @return 
	 * @return BlockChainTradeRecord
	 * @date 2018年8月24日 上午11:14:09
	 */
	BlockChainTradeRecord findByTxHash(String txHash);
}
