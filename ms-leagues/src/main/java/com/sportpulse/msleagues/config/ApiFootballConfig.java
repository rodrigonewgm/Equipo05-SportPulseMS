package com.sportpulse.msleagues.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiFootballConfig {

    @Value("${rapidapi.key}")
    private String apiKey;

    @Value("${rapidapi.base-url}")
    private String baseUrl;

    @Bean
    public WebClient apiFootballWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-apisports-key", apiKey)
                .build();
    }
}
