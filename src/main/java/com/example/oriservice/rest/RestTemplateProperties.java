package com.example.oriservice.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("http.rest")
@AllArgsConstructor
@NoArgsConstructor
public class RestTemplateProperties {

    private int readTimeout;

    private int connectTimeout;

    private int maxTotal;

    private int defaultMaxPerRoute;

    private int connectionRequestTimeout;

}
