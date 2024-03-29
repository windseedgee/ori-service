package com.example.oriservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

//    private final RequestContextHolder requestContextHolder;

    @Override
    public void handle(String message) {
        log.info(message);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        log.info("test request---:{},{}",request.getRequestURI(),request.getMethod());

//        HttpServletRequest httpServletRequest = RequestContextHolder.getRequest();
//        log.info("test request---:{},{}",httpServletRequest.getRequestURI(),httpServletRequest.getMethod());
    }

}
