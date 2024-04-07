package com.minibank.demo.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.demo.MiniBankConfigProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<String> availableCurrencies;
    private static Bank instance;

    private final MiniBankConfigProperties miniBankConfigProperties;

    private Bank(MiniBankConfigProperties miniBankConfigProperties) {
        availableCurrencies = new ArrayList<>();
        this.miniBankConfigProperties = miniBankConfigProperties;

        String uri = miniBankConfigProperties.apiUrl() +
                "/v1/currencies?api_key=" +
                miniBankConfigProperties.apiKey();

        WebClient.Builder builder = WebClient.builder();
        String response = builder.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class).block();

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                JsonNode currencies = jsonNode.get("response");

                for (JsonNode currencyNode : currencies) {
                    String shortCode = currencyNode.get("short_code").asText();
                    availableCurrencies.add(shortCode);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }

    public static Bank getInstance(MiniBankConfigProperties miniBankConfigProperties) {
        if(Bank.instance == null) {
            Bank.instance = new Bank(miniBankConfigProperties);
            System.out.println("Created new Bank instance...");
        }

        return Bank.instance;
    }

    public List<String> getAvailableCurrencies() {
        return availableCurrencies;
    }

}
