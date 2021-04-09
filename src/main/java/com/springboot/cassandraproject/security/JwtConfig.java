package com.springboot.cassandraproject.security;

import org.springframework.beans.factory.annotation.Value;

public class JwtConfig {

    @Value("${security.jwt.secret}")
    private String secret;

    public String getSecret() {
        return secret;
    }
}
