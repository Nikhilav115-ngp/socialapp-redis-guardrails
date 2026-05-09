package com.nikhil.socialapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SocialappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialappApplication.class, args);
	}

}
