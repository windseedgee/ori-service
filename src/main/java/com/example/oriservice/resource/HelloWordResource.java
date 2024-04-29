package com.example.oriservice.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/hello")
@Slf4j
public class HelloWordResource {

    @GetMapping
    public String helloWorld() {
        return "hello world";
    }

}
