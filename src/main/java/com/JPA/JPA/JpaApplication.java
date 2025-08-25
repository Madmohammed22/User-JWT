package com.JPA.JPA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.JPA.JPA.initializr"}) // Removed the extra space
public class JpaApplication {
	private final static Logger logger = LoggerFactory.getLogger(JpaApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}
}