package com.soft.eurekaFeign;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.soft.eurekaFeign.feign.TestLearnClient;

@SpringBootTest(classes = EurekaClientFeignApplication.class)
@RunWith(SpringRunner.class)
public class TestLearnClientTest {
	@Autowired
	TestLearnClient testLearnClient;
	
	@Test
	public void testTest(){
		String test = testLearnClient.test("王玮");
		System.out.println(test);
	}
}
