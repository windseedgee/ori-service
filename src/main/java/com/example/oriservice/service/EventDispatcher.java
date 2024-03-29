package com.example.oriservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void dispatchEvent(final String message) {
        applicationEventPublisher.publishEvent(message);
    }


}
