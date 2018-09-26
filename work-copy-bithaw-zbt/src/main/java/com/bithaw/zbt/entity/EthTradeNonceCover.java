package com.bithaw.zbt.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * eth_trade_nonce表实体类
 * @author WangWei
 * @date: 2018年8月24日 上午10:18:31
 * @version: v1.0.0
 * @Description: 存入表中的数据会定时尝试发送到公链上
 */
@Entity
@Table(name = "eth_trade_nonce_cover")
@ToString
@Data
@Builder
public class EthTradeNonceCover {
	public EthTradeNonceCover(){
		
	}
	
	public EthTradeNonceCover(Integer id, String coverNo, String orderNo, String fromAddress, Integer nonce, String toAddress, BigDecimal value, BigDecimal gasPrice, long gasLimit, String data, String rawTransaction, String txhash, int state, String stateErrorCode, String stateErrorMessage, Date createTime,Integer coverState,Integer finalState) {
		super();
		this.id = id;
		this.coverNo = coverNo;
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
		this.finalState = finalState;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;// 自动生成id

	@Column(name = "cover_no")
	private String coverNo;// 订单id
	
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

	@Column(name = "cover_state")
	private Integer coverState;
	
	@Column(name = "final_state")
	private Integer finalState;
}
