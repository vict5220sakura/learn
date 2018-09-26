/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月26日  下午4:54:02
 * @version   V 1.0
 */
package com.soft.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soft.entity.User;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月26日 下午4:54:02
 * @version  V 1.0
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer>{
	
}
