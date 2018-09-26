/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月19日  下午1:42:58
 * @version   V 1.0
 */
package com.start.bean;

import java.math.BigDecimal;

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
 * @date     2018年9月19日 下午1:42:58
 * @version  V 1.0
 */
@Data
@ToString
@Builder
@Entity
@Table(name="test_line_lock")
public class Money {
	public Money(){
		
	}
	
	public Money(Integer id, BigDecimal money) {
		super();
		this.id = id;
		this.money = money;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "money")
	private BigDecimal money;
}
