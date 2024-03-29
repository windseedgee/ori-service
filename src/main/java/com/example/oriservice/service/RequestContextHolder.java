package com.example.oriservice.service;

import jakarta.servlet.http.HttpServletRequest;

public class RequestContextHolder {

    private static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestHolder.get();
    }

    public static void clearRequest() {
        requestHolder.remove();
    }
}
