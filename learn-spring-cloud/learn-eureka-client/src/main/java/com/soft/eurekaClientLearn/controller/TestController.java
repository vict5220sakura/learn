package com.soft.eurekaClientLearn.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@Value("${server.hi}")
	private String hi;
	
	@PostMapping("/hi")
	public String test(){
		return "hello " + ",this port is " + hi;
	}
}
