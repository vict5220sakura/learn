package com.bithaw.zbt.mapper;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bithaw.zbt.entity.BlockChainRecord;

/**
 * @Description BlockChainRecord数据库Dao
 * @author   WangWei
 * @date     2018年8月24日 上午11:12:30
 * @version  V 1.0
 */
@Repository
public interface BlockChainRecordMapper extends JpaRepository<BlockChainRecord, BigInteger>{
	/**
	 * @author WangWei
	 * @Description 根据txHash查询实体类
	 * @method findByTxHash
	 * @param @param txHash
	 * @param @return 
	 * @return BlockChainRecord
	 * @date 2018年8月24日 上午11:12:49
	 */
	public BlockChainRecord findByTxHash(String txHash);
	
	/**
	 * @author WangWei
	 * @Description 根据扫描目标地址查询已经记录的最高区块
	 * @method getMaxBlockNumberByToAddress
	 * @param @param toAddress
	 * @param @return 
	 * @return BigInteger
	 * @date 2018年8月24日 上午11:13:05
	 */
	@Query(value = "select MAX(block_height) from block_chain_record where to_address = ?1",nativeQuery = true)
	public BigInteger getMaxBlockNumberByToAddress(String toAddress);
}
