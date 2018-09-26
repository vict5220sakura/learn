package com.bithaw.zbt.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignInterceptor implements RequestInterceptor{

	@Value(value="${bithaw.feign.token}")
	private String token;
	@Override
	public void apply(RequestTemplate template) {
		template.header("rpc-token", token);
	}

}
