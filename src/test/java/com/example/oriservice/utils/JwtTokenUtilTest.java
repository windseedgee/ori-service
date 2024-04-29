package com.example.oriservice.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class JwtTokenUtilTest {

    @Test
    public void generateToken() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "ori-test");
        claims.put("created", new Date());
        String token = jwtTokenUtil.generateToken(claims);
        log.info("generateToken:{}", token);
    }

}
