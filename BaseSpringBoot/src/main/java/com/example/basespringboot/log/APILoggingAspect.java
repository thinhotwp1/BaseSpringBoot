package com.example.basespringboot.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Aspect
@Component
@Log4j2
public class APILoggingAspect {
    // Thay execution("") bằng path controller hiện tại của project
    @Before("execution(* com.example.basespringboot.controller.*.*(..))")
    public void logApiCall(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Truyền path và uuid vào log context để trace log, vì chặn ở đầu controller
        //      nên khi đi vào Global Exception sẽ không đi vào đây nữa
        ThreadContext.put("uuid", UUID.randomUUID().toString());
        ThreadContext.put("path",request.getRequestURI());

        // Get body request
        Object[] args = joinPoint.getArgs();
        StringBuilder body = new StringBuilder();
        for (Object requests : args) {
            body.append(requests.toString());
        }

        // Log request before to controller
        log.info("______________________________________________________________________________________________________________________________________");
        log.info("UUID: " + ThreadContext.get("uuid"));
        log.info("PATH: " + ThreadContext.get("path"));
        // Nếu sử dụng cùng với Security và sử dụng authentication thì gán username vào
        log.info("Username : " + SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("RequestBody : " + body);
    }
}
