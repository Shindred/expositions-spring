package com.myproject.expo.expositions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
//@ServletComponentScan
public class ExpositionsSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpositionsSpringApplication.class, args);
	}

}
