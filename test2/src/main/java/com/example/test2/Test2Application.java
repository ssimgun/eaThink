package com.example.test2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude= SecurityAutoConfiguration.class)
public class Test2Application {

	public static void main(String[] args) {
		SpringApplication.run(Test2Application.class, args);
	}

}
