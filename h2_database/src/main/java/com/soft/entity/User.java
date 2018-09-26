/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月26日  下午4:50:34
 * @version   V 1.0
 */
package com.soft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.ToString;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月26日 下午4:50:34
 * @version  V 1.0
 */
@Entity
@Table(name = "user",
	uniqueConstraints = {@UniqueConstraint(columnNames={"username"})},
	indexes = {@Index (columnList = "username")})
@Data
@ToString
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String username;
	
	private String password;
}
