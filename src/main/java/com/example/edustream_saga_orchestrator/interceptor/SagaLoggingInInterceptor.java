package com.example.edustream_saga_orchestrator.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class SagaLoggingInInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object Handler){

        log.info("-> -> -> -> Incoming request: Method-{} | URI-{}", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Object Handler,
                                   Exception e) {

        log.info("<- <- <- <- Outgoing response: Status-{} | URI-{}", response.getStatus(), request.getRequestURI());
    }

}
