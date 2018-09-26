package com.bithaw.zbt.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 区块对应eth节点扫描实体类
 * @author WangWei
 * @date: 2018年8月24日 上午10:17:21
 * @version: v1.0.0
 * @Description: 扫描区块后的数据
 */
public class BlockEntity {
	private BigInteger blockNumber;// 区块高度
	private String blockHash;// 区块hash
	private List<String> transactionHashList;// 区块中全部交易
	private Date date;// 确认时间

	public BigInteger getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(BigInteger blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public List<String> getTransactionHashList() {
		return transactionHashList;
	}

	public void setTransactionHashList(List<String> transactionHashList) {
		this.transactionHashList = transactionHashList;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
