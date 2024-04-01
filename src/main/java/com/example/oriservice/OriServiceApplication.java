package com.example.oriservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "studentAuditorAware")
//@EnableJpaAuditing
@EnableWebMvc
public class OriServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OriServiceApplication.class, args);
    }

}
