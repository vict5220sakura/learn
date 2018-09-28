/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午2:25:53
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
 * @date     2018年9月5日 下午2:25:53
 * @version  V 1.0
 */
@Entity
@Table(name="btc_trade")
@Data
@Builder
@ToString
public class BtcTrade {
	
	public BtcTrade(){}
	
	
	
	public BtcTrade(BigInteger id, String uuid, String toAddress, BigDecimal amount, BigDecimal fees, String rawTransaction, String txhash, int state, Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.toAddress = toAddress;
		this.amount = amount;
		this.fees = fees;
		this.rawTransaction = rawTransaction;
		this.txhash = txhash;
		this.state = state;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "to_address")
	private String toAddress;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "fees")
	private BigDecimal fees;
	
	@Column(name = "raw_transaction")
	private String rawTransaction;
	
	@Column(name = "txhash")
	private String txhash;
	
	/**
	 * state : 0创建交易成功,1成功,2失败,3构建交易成功,4签名成功,5写入txhash成功
	 */
	@Column(name = "state")
	private int state;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "update_time")
	private Date updateTime;
}
