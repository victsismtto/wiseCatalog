package com.code.elevate.wise.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient openLibraryWebClient() {
        return WebClient.builder()
                .baseUrl("https://openlibrary.org/subjects/")
                .build();
    }
}
