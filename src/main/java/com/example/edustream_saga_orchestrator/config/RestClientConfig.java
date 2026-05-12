package com.example.edustream_saga_orchestrator.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientConfig {

    // Student MicroService Base URL
    @Value("${services.student.url}")
    private String studentBackendUrl;

    // Course MicroService Base URL
    @Value("${services.course.url}")
    private String courseBackendUrl;


    // Create a Separate RestClient for Student MicroService
    @Bean
    @Qualifier("studentRestClient")
    public RestClient studentRestClient() {
        return RestClient.builder()
                .baseUrl(studentBackendUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Set the default return type to JSON
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE) // Set the default accept header to JSON
                .build();

    }

    // Create a Separate RestClient for Course MicroService
    @Bean
    @Qualifier("courseRestClient")
    public RestClient courseRestClient() {
        return RestClient.builder()
                .baseUrl(courseBackendUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
