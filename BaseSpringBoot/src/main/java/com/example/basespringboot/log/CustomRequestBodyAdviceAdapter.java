package com.example.basespringboot.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.UUID;

@ControllerAdvice
@Log4j2
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(@NotNull MethodParameter methodParameter,
                            @NotNull Type type,
                            @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public @NotNull Object afterBodyRead(Object body,
                                         @NotNull HttpInputMessage inputMessage,
                                         @NotNull MethodParameter parameter,
                                         @NotNull Type targetType,
                                         @NotNull Class<? extends HttpMessageConverter<?>> converterType) {

        ThreadContext.put("uuid", UUID.randomUUID().toString());
        ThreadContext.put("path", httpServletRequest.getRequestURI());
        ThreadContext.put("request_body", body.toString());

        // Log request before to controller
        log.info("________________________________________________________________REQUEST_______________________________________________________________");
        log.info("TYPE: REQUEST");
        log.info("UUID: " + ThreadContext.get("uuid"));
        log.info("PATH: " + ThreadContext.get("path"));
        // Nếu sử dụng cùng với Security và sử dụng authentication thì gán username vào
        log.info("Username : " + SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("RequestBody : " + body);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
