package com.bithaw.zbt.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.ToString;

/**
 * eth_trade_nonce表实体类
 * @author WangWei
 * @date: 2018年8月24日 上午10:18:31
 * @version: v1.0.0
 * @Description: 存入表中的数据会定时尝试发送到公链上
 */
@Entity
@Table(name = "eth_trade_nonce")
@ToString
public class EthTradeNonce {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;// 自动生成id

	@Column(name = "order_no")
	private String orderNo;// 订单id
	
	@Column(name = "from_address")
	private String fromAddress;// fromAddress
	
	@Column(name = "nonce")
	private Integer nonce;// nonce
	
	@Column(name = "to_address")
	private String toAddress;// toAddress
	
	@Column(name = "value")
	private BigDecimal value;// ETH数额(ETH)
	
	@Column(name = "gas_price")
	private BigDecimal gasPrice;// gasPrice(gwei)
	
	@Column(name = "gas_limit")
	private long gasLimit;// gasLimit
	
	@Column(name = "data")
	private String data;// data,交易data
	
	@Column(name = "raw_transaction")
	private String rawTransaction;// 签名后的rawTransaction
	
	@Column(name = "txhash")
	private String txhash;// 交易hash
	
	@Column(name = "state")
	private int state;// 0未知,1:成功(6区块确认)
	
	@Column(name = "state_error_code")
	private String stateErrorCode;// 广播错误代码
	
	@Column(name = "state_error_message")
	private String stateErrorMessage;// 广播错误信息
	
	@Column(name = "create_time")
	private Date createTime;// 创建时间

	/**
	 * coverState : 覆盖交易装填标记;null/0:未发生覆盖,1:覆盖标记;2:发生覆盖且成功;3:发生覆盖且失败
	 */
	@Column(name = "cover_state")
	private Integer coverState;
	
	public EthTradeNonce(){
		
	}
	
	public EthTradeNonce(Integer id, String orderNo, String fromAddress, Integer nonce, String toAddress, BigDecimal value, BigDecimal gasPrice, long gasLimit, String data, String rawTransaction, String txhash, int state, String stateErrorCode, String stateErrorMessage, Date createTime,Integer coverState) {
		super();
		this.id = id;
		this.orderNo = orderNo;
		this.fromAddress = fromAddress;
		this.nonce = nonce;
		this.toAddress = toAddress;
		this.value = value;
		this.gasPrice = gasPrice;
		this.gasLimit = gasLimit;
		this.data = data;
		this.rawTransaction = rawTransaction;
		this.txhash = txhash;
		this.state = state;
		this.stateErrorCode = stateErrorCode;
		this.stateErrorMessage = stateErrorMessage;
		this.createTime = createTime;
		this.coverState = coverState;
	}

	public static class Builder{
		public EthTradeNonce build(){
			return new EthTradeNonce(id, orderNo, fromAddress, nonce, toAddress, value, gasPrice, gasLimit, data, rawTransaction, txhash, state, stateErrorCode, stateErrorMessage, createTime,coverState);
		}
		private Integer id;// 自动生成id
		private String orderNo;// 订单id
		private String fromAddress;// fromAddress
		private Integer nonce;// nonce
		private String toAddress;// toAddress
		private BigDecimal value;// ETH数额(ETH)
		private BigDecimal gasPrice;// gasPrice(gwei)
		private long gasLimit;// gasLimit
		private String data;// data,交易data
		private String rawTransaction;// 签名后的rawTransaction
		private String txhash;// 交易hash
		private int state;// 0未知,1:成功(6区块确认)
		private String stateErrorCode;// 广播错误代码
		private String stateErrorMessage;// 广播错误信息
		private Date createTime;// 创建时间
		private Integer coverState;
		public Builder setId(Integer id) {
			this.id = id;
			return this;
		}
		public Builder setOrderNo(String orderNo) {
			this.orderNo = orderNo;
			return this;
		}
		public Builder setFromAddress(String fromAddress) {
			this.fromAddress = fromAddress;
			return this;
		}
		public Builder setNonce(Integer nonce) {
			this.nonce = nonce;
			return this;
		}
		public Builder setToAddress(String toAddress) {
			this.toAddress = toAddress;
			return this;
		}
		public Builder setValue(BigDecimal value) {
			this.value = value;
			return this;
		}
		public Builder setGasPrice(BigDecimal gasPrice) {
			this.gasPrice = gasPrice;
			return this;
		}
		public Builder setGasLimit(long gasLimit) {
			this.gasLimit = gasLimit;
			return this;
		}
		public Builder setData(String data) {
			this.data = data;
			return this;
		}
		public Builder setRawTransaction(String rawTransaction) {
			this.rawTransaction = rawTransaction;
			return this;
		}
		public Builder setTxhash(String txhash) {
			this.txhash = txhash;
			return this;
		}
		public Builder setState(int state) {
			this.state = state;
			return this;
		}
		public Builder setStateErrorCode(String stateErrorCode) {
			this.stateErrorCode = stateErrorCode;
			return this;
		}
		public Builder setStateErrorMessage(String stateErrorMessage) {
			this.stateErrorMessage = stateErrorMessage;
			return this;
		}
		public Builder setCreateTime(Date createTime) {
			this.createTime = createTime;
			return this;
		}
		public Builder setCoverState(Integer coverState) {
			this.coverState = coverState;
			return this;
		}
		
		
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Integer getNonce() {
		return nonce;
	}

	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(BigDecimal gasPrice) {
		this.gasPrice = gasPrice;
	}

	public long getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(long gasLimit) {
		this.gasLimit = gasLimit;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getRawTransaction() {
		return rawTransaction;
	}

	public void setRawTransaction(String rawTransaction) {
		this.rawTransaction = rawTransaction;
	}

	public String getTxhash() {
		return txhash;
	}

	public void setTxhash(String txhash) {
		this.txhash = txhash;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStateErrorCode() {
		return stateErrorCode;
	}

	public void setStateErrorCode(String stateErrorCode) {
		this.stateErrorCode = stateErrorCode;
	}

	public String getStateErrorMessage() {
		return stateErrorMessage;
	}

	public void setStateErrorMessage(String stateErrorMessage) {
		this.stateErrorMessage = stateErrorMessage;
	}

	public Integer getCoverState() {
		return coverState;
	}

	public void setCoverState(Integer coverState) {
		this.coverState = coverState;
	}

	
	
}
