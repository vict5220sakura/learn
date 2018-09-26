package com.bithaw.zbt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: 
 * @Date: 
 * @Description: 
 */
@Component
@Slf4j
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, Object o) throws Exception {

        String token = httpServletRequest.getHeader("access-token");
       // httpServletRequest.addHeader("Connection","close");
////        log.info("headers:{}", JSONObject.toJSONString(httpServletRequest.getHeaderNames()));
//
//        RequestThread.setRequestTime(System.currentTimeMillis());
//        LoginRequire loginRequire = ((HandlerMethod) o).getMethodAnnotation(LoginRequire.class);
//        if (loginRequire != null && !loginRequire.require()) {
//            return true;
//        }
//        if (StringUtils.isBlank(token)) {
//            printMessage(httpServletResponse, R.of(ResponseEnum.NOT_LOGIN));
//            return false;
//        }
//        Token t = JSON.parseObject(new String(Base64.decodeBase64(token)), Token.class);
//        if (t == null) {
//            printMessage(httpServletResponse, R.of(ResponseEnum.NOT_LOGIN));
//            return false;
//        }
//        RequestThread.setToken(t);
        return true;
    }

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
