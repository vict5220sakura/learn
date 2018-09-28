/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月5日  下午3:56:19
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

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月5日 下午3:56:19
 * @version  V 1.0
 */
@Entity
@Table(name="btc_trade_output")
@Data
@Builder
public class BtcTradeOutput {
	
	public BtcTradeOutput() {
		super();
	}
	
	public BtcTradeOutput(BigInteger id, String uuid, String address, BigDecimal amount, Date createTime) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.address = address;
		this.amount = amount;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "create_time")
	private Date createTime;
}
