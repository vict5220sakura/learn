package com.bithaw.zbt.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 区块交易信息实体类表
 * @author WangWei
 * @date: 2018年8月24日 上午10:16:07
 * @version: v1.0.0
 * @Description: 本地扫描内容实体类
 */
@Entity
@Table(name="block_chain_record")
public class BlockChainRecord {
	
	
	public BlockChainRecord(){}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;//自动生成id
	
	@Column(name = "block_height")
	private BigInteger blockHeight;//区块高度
	
	@Column(name = "add_time")
	private BigInteger addTime;//区块确认时间
	
	@Column(name = "tx_hash")
	private String txHash;//交易hash
	
	@Column(name = "from_address")
	private String fromAddress;//发送方地址
	
	@Column(name = "to_address")
	private String toAddress;//eth收款方地址
	
	@Column(name = "content")
	private String content;//发送附加数据 原始input data数据16
	
	@Column(name = "amount")
	private BigDecimal amount;//发送eth数量,单位 eth
	
	@Column(name = "coin_type")
	private String coinType = "ETH";//币类型,默认ETH
	
	@Column(name = "actual_service_fee")
	private BigDecimal actualServiceFee;//手续费
	
	@Column(name = "status")
	private Integer status;//状态 1 成功(6区块确认) 2 失败(指的是合约调用失败) 0 确认中
	
	@Column(name = "contract_to_address")
	private String contractToAddress;//合约zbt发送方地址
	
	@Column(name = "contract_amount")
	private BigDecimal contractAmount;//合约发送zbt数目

	@Transient
	private BlockChainTradeRecord blockChainTradeRecord;//合约交易实体
	
	
	
	public BlockChainRecord(BigInteger id, BigInteger blockHeight, BigInteger addTime, String txHash, String fromAddress, String toAddress, String content, BigDecimal amount, String coinType, BigDecimal actualServiceFee, Integer status, String contractToAddress, BigDecimal contractAmount, BlockChainTradeRecord blockChainTradeRecord) {
		super();
		this.id = id;
		this.blockHeight = blockHeight;
		this.addTime = addTime;
		this.txHash = txHash;
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.content = content;
		this.amount = amount;
		this.coinType = coinType;
		this.actualServiceFee = actualServiceFee;
		this.status = status;
		this.contractToAddress = contractToAddress;
		this.contractAmount = contractAmount;
		this.blockChainTradeRecord = blockChainTradeRecord;
	}

	/**
	 * 构造器
	 * @author WangWei
	 * @date: 2018年8月24日 上午10:16:37
	 * @version: v1.0.0
	 * @Description:
	 */
	public static class Builder{
		private BigInteger id;//自动生成id
		private BigInteger blockHeight;//区块高度
		private BigInteger addTime;//区块确认时间
		private String txHash;//交易hash
		private String fromAddress;//发送方地址
		private String toAddress;//eth收款方地址
		private String content;//发送附加数据 原始input data数据16
		private BigDecimal amount;//发送eth数量,单位 eth
		private String coinType = "ETH";//币类型,默认ETH
		private BigDecimal actualServiceFee;//手续费
		private Integer status;//状态 1 成功(6区块确认) 2 失败(指的是合约调用失败) 0 确认中
		private String contractToAddress;//合约zbt发送方地址
		private BigDecimal contractAmount;//合约发送zbt数目
		private BlockChainTradeRecord blockChainTradeRecord;//合约交易实体
		
		public BlockChainRecord build(){
			return new BlockChainRecord(id, blockHeight, addTime, txHash, fromAddress, toAddress, content, amount, coinType, actualServiceFee, status, contractToAddress, contractAmount, blockChainTradeRecord);
		}
		
		public Builder setId(BigInteger id) {
			this.id = id;
			return this;
		}
		public Builder setBlockHeight(BigInteger blockHeight) {
			this.blockHeight = blockHeight;
			return this;
		}
		public Builder setAddTime(BigInteger addTime) {
			this.addTime = addTime;
			return this;
		}
		public Builder setTxHash(String txHash) {
			this.txHash = txHash;
			return this;
		}
		public Builder setFromAddress(String fromAddress) {
			this.fromAddress = fromAddress;
			return this;
		}
		public Builder setToAddress(String toAddress) {
			this.toAddress = toAddress;
			return this;
		}
		public Builder setContent(String content) {
			this.content = content;
			return this;
		}
		public Builder setAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}
		public Builder setCoinType(String coinType) {
			this.coinType = coinType;
			return this;
		}
		public Builder setActualServiceFee(BigDecimal actualServiceFee) {
			this.actualServiceFee = actualServiceFee;
			return this;
		}
		public Builder setStatus(Integer status) {
			this.status = status;
			return this;
		}
		public Builder setContractToAddress(String contractToAddress) {
			this.contractToAddress = contractToAddress;
			return this;
		}
		public Builder setContractAmount(BigDecimal contractAmount) {
			this.contractAmount = contractAmount;
			return this;
		}
		public Builder setBlockChainTradeRecord(BlockChainTradeRecord blockChainTradeRecord) {
			this.blockChainTradeRecord = blockChainTradeRecord;
			return this;
		}
		
	}
	
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getBlockHeight() {
		return blockHeight;
	}

	public void setBlockHeight(BigInteger blockHeight) {
		this.blockHeight = blockHeight;
	}

	public BigInteger getAddTime() {
		return addTime;
	}

	public void setAddTime(BigInteger addTime) {
		this.addTime = addTime;
	}

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCoinType() {
		return coinType;
	}

	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}

	public BigDecimal getActualServiceFee() {
		return actualServiceFee;
	}

	public void setActualServiceFee(BigDecimal actualServiceFee) {
		this.actualServiceFee = actualServiceFee;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContractToAddress() {
		return contractToAddress;
	}

	public void setContractToAddress(String contractToAddress) {
		this.contractToAddress = contractToAddress;
	}

	public BigDecimal getContractAmount() {
		return contractAmount;
	}

	public void setContractAmount(BigDecimal contractAmount) {
		this.contractAmount = contractAmount;
	}

	public BlockChainTradeRecord getBlockChainTradeRecord() {
		return blockChainTradeRecord;
	}

	public void setBlockChainTradeRecord(BlockChainTradeRecord blockChainTradeRecord) {
		this.blockChainTradeRecord = blockChainTradeRecord;
	}
	
	
}
