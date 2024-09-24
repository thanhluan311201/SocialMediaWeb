package com.smw.SocialMediaWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.smw.SocialMediaWeb")
public class SocialMediaWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaWebApplication.class, args);
	}

}
