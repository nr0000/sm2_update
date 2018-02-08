package com.ssh.sm2_update;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Sm2UpdateApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sm2UpdateApplication.class, args);
	}
}
