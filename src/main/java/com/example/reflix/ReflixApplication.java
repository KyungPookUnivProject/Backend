package com.example.reflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

//@SpringBootApplication
@EnableScheduling
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ReflixApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReflixApplication.class, args);
    }

}
