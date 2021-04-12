package com.springboot.cassandraproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;

@SpringBootApplication
public class CassandraProjectApplication {

    public static void main(String[] args) throws UnsupportedEncodingException {
        SpringApplication.run(CassandraProjectApplication.class, args);
    }
}
