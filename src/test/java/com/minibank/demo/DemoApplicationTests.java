package com.minibank.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void checkHealth() {
		MiniBankApp bankApp = new MiniBankApp();
		String message = bankApp.health();
		Assertions.assertEquals("Server is up and running", message);
	}

}
