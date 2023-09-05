package com.example.basespringboot.log;

import com.example.basespringboot.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Log4j2
public class APILoggingAspect {
    // AOP (Lập trình hướng khía cạnh), tách các việc như log,... sang một nơi khác ngoài controller, service, ...

    // Xử lý trước khi vào controller
    @Before("execution(* com.example.basespringboot.controller.*.*(..))")
    public void logApiBeforeController(@NotNull JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Get body request
        Object[] args = joinPoint.getArgs();
        StringBuilder body = new StringBuilder();
        for (Object requests : args) {
            body.append(requests.toString());
        }

        ThreadContext.put("uuid", UUID.randomUUID().toString());
        ThreadContext.put("path", request.getRequestURI());
        ThreadContext.put("request_body", body.toString());

        // Log request before to controller
        log.info("________________________________________________________________REQUEST_______________________________________________________________");
        log.info("TYPE: REQUEST");
        log.info("UUID: " + ThreadContext.get("uuid"));
        log.info("PATH: " + ThreadContext.get("path"));
        // Nếu sử dụng cùng với Security và sử dụng authentication thì gán username vào
        log.info("Username : " + SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("RequestBody : " + body);
    }

    // Xử lý trước khi vào GlobalException
    @Before("execution(* com.example.basespringboot.exception.*.*(..))")
    public void logApiBeforeGlobalException(@NotNull JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Trường hợp nếu lỗi khi chưa tới controller, phải đặt uuid và path khác
        if (StringUtil.isNullOrEmpty(ThreadContext.get("uuid"))) {
            ThreadContext.put("uuid", UUID.randomUUID().toString());
        }
        ThreadContext.put("path", request.getRequestURI());

    }


}
