package com.minibank.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MiniBankApp {

	public static void main(String[] args) {
		SpringApplication.run(MiniBankApp.class, args);
	}

	@GetMapping("api/health")
	public String health() {
		return "Server is up and running";
	}
}
