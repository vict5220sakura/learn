package com.bithaw.zbt.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bithaw.common.utils.WebResult;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: lijy
 * @Date: 2018/5/5 16:20
 * @Description:
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public WebResult defaultExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("uri={}, method={}", req.getRequestURI(), req.getMethod(), e);
        return WebResult.buildFail("1","系统异常，请联系管理员");
    }

}
