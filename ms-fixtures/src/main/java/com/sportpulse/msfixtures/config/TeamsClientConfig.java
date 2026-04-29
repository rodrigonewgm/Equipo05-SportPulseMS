package com.sportpulse.msfixtures.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TeamsClientConfig {

    @Value("${services.teams-url}")
    private String teamsUrl;

    @Bean
    public WebClient teamsWebClient() {
        return WebClient.builder()
                .baseUrl(teamsUrl)
                .build();
    }
}
