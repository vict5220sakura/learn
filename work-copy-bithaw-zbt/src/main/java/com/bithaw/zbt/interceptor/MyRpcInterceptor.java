package com.bithaw.zbt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bithaw.common.interceptor.RpcInterceptor;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class MyRpcInterceptor extends RpcInterceptor{
	
	@Value("${bithaw.feign.token}")
	private String rpcToken;
	{
		log.info("--------------------------------------------");
	}
	
	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, Object o) throws Exception {
		super.rpcToken = this.rpcToken;
		return super.preHandle(httpServletRequest, httpServletResponse, o);
        
    }

    
	
}
