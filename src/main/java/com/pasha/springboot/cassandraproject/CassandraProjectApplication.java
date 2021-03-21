package com.pasha.springboot.cassandraproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import java.util.logging.LogManager;
//import java.util.logging.Logger;

@SpringBootApplication
public class CassandraProjectApplication {

	private static final Logger logger = LogManager.getLogger(CassandraProjectApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(CassandraProjectApplication.class, args);

	}

}
