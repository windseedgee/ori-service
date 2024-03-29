package com.example.oriservice.resource;


import com.example.oriservice.service.VirtualThreadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/virtualthread")
@Slf4j
public class VirtualThreadResource {

    private final VirtualThreadService virtualThreadService;

    @GetMapping
    public void mockConcurrentRequest(){
        virtualThreadService.mockConcurrentRequest();
    }

    @GetMapping("/merge")
    public int test(@RequestParam("fileUrls") List<String> fileUrls,
        @RequestParam("fileName") String fileName, @RequestParam("shipperId") Long shipperId) {
        try {
            return virtualThreadService.mockMergePdfOOM(fileUrls, fileName, shipperId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
