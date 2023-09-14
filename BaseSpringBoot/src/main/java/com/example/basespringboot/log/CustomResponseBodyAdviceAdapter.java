package com.example.basespringboot.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.UUID;

@ControllerAdvice
@Log4j2
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(@NotNull MethodParameter methodParameter,
                            @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NotNull MethodParameter methodParameter,
                                  @NotNull MediaType mediaType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> aClass,
                                  @NotNull ServerHttpRequest serverHttpRequest,
                                  @NotNull ServerHttpResponse serverHttpResponse) {

        // Log response
        log.info("______________________________RESPONSE_________________________________________________________________________________________________");
        log.info("UUID: " + ThreadContext.get("uuid"));
        log.info("PATH: " + ThreadContext.get("path"));
        // Nếu sử dụng cùng với Security và sử dụng authentication thì gán username vào
        log.info("USER: " + SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("RESPONSE: " + body);
        return body;
    }
}