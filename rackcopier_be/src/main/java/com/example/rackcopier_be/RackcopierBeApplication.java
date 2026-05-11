package com.example.rackcopier_be;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RackcopierBeApplication {

	private static Logger log = LoggerFactory.getLogger(RackcopierBeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RackcopierBeApplication.class, args);
		log.info("App started successfully");
	}

}
