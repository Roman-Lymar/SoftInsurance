package com.springboot.cassandraproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

import java.io.UnsupportedEncodingException;

@SpringBootApplication
public class CassandraProjectApplication {

    public static void main(String[] args) throws UnsupportedEncodingException {
        SpringApplication.run(CassandraProjectApplication.class, args);
    }

}
