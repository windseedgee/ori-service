package com.example.oriservice.resource;

import com.example.oriservice.utils.JwtTokenUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/token")
@Slf4j
public class TokenResource {

    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public String getToken() {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "ori-test");
        claims.put("created", new Date());
        String token = jwtTokenUtil.generateToken(claims);
        log.info("generateToken:{}", token);
        return token;
    }
}
