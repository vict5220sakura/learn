/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午3:37:18
 * @version   V 1.0
 */
package com.bithaw.btc.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午3:37:18
 * @version  V 1.0
 */
@Entity
@Table(name="btc_trade_input")
@Data
@Builder
@ToString
public class BtcTradeInput {
	
	public BtcTradeInput(){}

	public BtcTradeInput(BigInteger id, String uuid, String txId, int txIndex, BigDecimal amount, String address, int confirmations, String scriptPubkey, Date createTime) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.txId = txId;
		this.txIndex = txIndex;
		this.amount = amount;
		this.address = address;
		this.confirmations = confirmations;
		this.scriptPubkey = scriptPubkey;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "tx_id")
	private String txId;
	
	@Column(name = "tx_index")
	private int txIndex;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "confirmations")
	private int confirmations;
	
	@Column(name = "script_pubkey")
	private String scriptPubkey;
	
	@Column(name = "create_time")
	private Date createTime;
}
