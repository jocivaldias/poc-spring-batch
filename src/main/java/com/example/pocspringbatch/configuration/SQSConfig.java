package com.example.pocspringbatch.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration

public class SQSConfig {

    private final String endpoint;

    public SQSConfig(@Value("${aws.sqs.endpoint}") String endpoint) {
        this.endpoint = endpoint;
    }

    @Bean
    public SqsAsyncClient sqsClient() {
        return SqsAsyncClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(endpoint))
                .build();
    }
}
