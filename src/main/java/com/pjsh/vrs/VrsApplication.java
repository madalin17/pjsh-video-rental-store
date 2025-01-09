package com.pjsh.vrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.pjsh.vrs")
@EnableAsync
@EntityScan("com.pjsh.vrs.entity")
@EnableJpaRepositories("com.pjsh.vrs.storage")
public class VrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VrsApplication.class, args);
	}

}
