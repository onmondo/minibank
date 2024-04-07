package com.minibank.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mini-bank")
public record MiniBankConfigProperties(String apiUrl, String apiKey) {
}
