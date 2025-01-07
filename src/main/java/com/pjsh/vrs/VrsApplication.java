package com.pjsh.vrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VrsApplication.class, args);
	}

}
