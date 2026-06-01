package com.bank.msaccount.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a WebClient bean
     *
     * @return {@link WebClient} instance
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }
}
