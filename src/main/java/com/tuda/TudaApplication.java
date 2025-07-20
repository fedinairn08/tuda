package com.tuda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TudaApplication {

	public static void main(String[] args) {

		SpringApplication.run(TudaApplication.class, args);

	}

}
