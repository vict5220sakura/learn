/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月26日  下午4:55:19
 * @version   V 1.0
 */
package com.soft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soft.dao.UserDao;
import com.soft.entity.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月26日 下午4:55:19
 * @version  V 1.0
 */
@Slf4j
@RestController
public class UserController {
	@Autowired
	private UserDao userDao;
	
	@GetMapping("/user/getAll")
	public String getAll(){
		List<User> findAll = userDao.findAll();
		String str = "";
		for(User user : findAll){
			str += user.toString();
		}
		return str;
	}
	@PostMapping("/user/add")
	public String add(@RequestParam("username") String username,@RequestParam("password") String password){
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		try {
			userDao.save(user);
		} catch (Exception e) {
			log.error("创建用户失败",e);
			return "FAIL";
		}
		return "SUCCESS";
	}
}
