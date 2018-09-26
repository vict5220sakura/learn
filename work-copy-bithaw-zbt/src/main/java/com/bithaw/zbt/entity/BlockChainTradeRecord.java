package com.bithaw.zbt.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 本地扫描eth节点交易实体类
 * @author WangWei
 * @date: 2018年8月24日 上午10:16:49
 * @version: v1.0.0
 * @Description: 对应于数据库,映射
 */
@Entity
@Table(name="block_chain_trade_record")
public class BlockChainTradeRecord {
	public BlockChainTradeRecord(){
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;//自动生成id
	
	@Column(name = "merchant_id")
	private BigInteger merchantId;
	
	@Column(name = "user_id")
	private BigInteger userId;
	
	@Column(name = "from_address")
	private String fromAddress;
	
	@Column(name = "to_address")
	private String toAddress;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "coin_type")
	private String coinType = "ZBT";
	
	@Column(name = "actual_service_fee")
	private BigDecimal actualServiceFee;
	
	@Column(name = "tx_hash")
	private String txHash;
	
	@Column(name = "block_height")
	private BigInteger blockHeight;
	
	@Column(name = "solidity_address")
	private String solidityAddress;
	
	@Column(name = "notify_url")
	private String notifyUrl;
	
	@Column(name = "notify_status")
	private Integer notifyStatus;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "add_time")
	private BigInteger addTime;
	
	public BlockChainTradeRecord(BigInteger id, BigInteger merchantId, BigInteger userId, String fromAddress, String toAddress, BigDecimal amount, String coinType, BigDecimal actualServiceFee, String txHash, BigInteger blockHeight, String solidityAddress, String notifyUrl, Integer notifyStatus, Integer status, BigInteger addTime) {
		super();
		this.id = id;
		this.merchantId = merchantId;
		this.userId = userId;
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.amount = amount;
		this.coinType = coinType;
		this.actualServiceFee = actualServiceFee;
		this.txHash = txHash;
		this.blockHeight = blockHeight;
		this.solidityAddress = solidityAddress;
		this.notifyUrl = notifyUrl;
		this.notifyStatus = notifyStatus;
		this.status = status;
		this.addTime = addTime;
	}

	public static class Builder{
		private BigInteger id;//自动生成id
		private BigInteger merchantId;
		private BigInteger userId;
		private String fromAddress;
		private String toAddress;
		private BigDecimal amount;
		private String coinType = "ZBT";
		private BigDecimal actualServiceFee;
		private String txHash;
		private BigInteger blockHeight;
		private String solidityAddress;
		private String notifyUrl;
		private Integer notifyStatus;
		private Integer status;
		private BigInteger addTime;
		public BlockChainTradeRecord build(){
			return new BlockChainTradeRecord(id, merchantId, userId, fromAddress, toAddress, amount, coinType, actualServiceFee, txHash, blockHeight, solidityAddress, notifyUrl, notifyStatus, status, addTime);
		}
		public Builder setId(BigInteger id) {
			this.id = id;
			return this;
		}
		public Builder setMerchantId(BigInteger merchantId) {
			this.merchantId = merchantId;
			return this;
		}
		public Builder setUserId(BigInteger userId) {
			this.userId = userId;
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
		public Builder setTxHash(String txHash) {
			this.txHash = txHash;
			return this;
		}
		public Builder setBlockHeight(BigInteger blockHeight) {
			this.blockHeight = blockHeight;
			return this;
		}
		public Builder setSolidityAddress(String solidityAddress) {
			this.solidityAddress = solidityAddress;
			return this;
		}
		public Builder setNotifyUrl(String notifyUrl) {
			this.notifyUrl = notifyUrl;
			return this;
		}
		public Builder setNotifyStatus(Integer notifyStatus) {
			this.notifyStatus = notifyStatus;
			return this;
		}
		public Builder setStatus(Integer status) {
			this.status = status;
			return this;
		}
		public Builder setAddTime(BigInteger addTime) {
			this.addTime = addTime;
			return this;
		}
	}
	
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(BigInteger merchantId) {
		this.merchantId = merchantId;
	}

	public BigInteger getUserId() {
		return userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
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

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public BigInteger getBlockHeight() {
		return blockHeight;
	}

	public void setBlockHeight(BigInteger blockHeight) {
		this.blockHeight = blockHeight;
	}

	public String getSolidityAddress() {
		return solidityAddress;
	}

	public void setSolidityAddress(String solidityAddress) {
		this.solidityAddress = solidityAddress;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Integer getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(Integer notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigInteger getAddTime() {
		return addTime;
	}

	public void setAddTime(BigInteger addTime) {
		this.addTime = addTime;
	}
}
