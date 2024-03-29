package com.example.oriservice.service;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

public interface VirtualThreadService {

    void mockConcurrentRequest();

    int mockMergePdfOOM(List<String> fileUrls, String fileName, Long shipperId);

}
