package com.aidmeo.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;



@Configuration
public class EuriApiConfig {

    @Value("${euri.api.url}")
    private String euriUrl;

    @Bean
    public WebClient openAIClient() {
        return WebClient.builder()
                .baseUrl(euriUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
