package com.example.oriservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OriEventListener {

    private final EventService eventService;

    @Async
    @EventListener
    public void onApplicationEvent(String message) {
        eventService.handle(message);
    }

}
