package com.soft.eurekaFeign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soft.eurekaFeign.feign.TestLearnClient;

@RestController
public class TestFeignController {
	@Autowired
	TestLearnClient testLearnClient;
	
	@PostMapping("/hi")
	public String testFeign(@RequestParam("name")String name){
		return testLearnClient.test(name);
	}
}
