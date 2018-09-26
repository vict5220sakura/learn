package com.soft.eurekaFeign.feign;

import org.springframework.stereotype.Component;

@Component
public class TestLearnClientFallback implements TestLearnClient {

	@Override
	public String test(String name) {
		return "sorry " + name;
	}

}
