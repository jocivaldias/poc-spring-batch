package com.example.pocspringbatch;

import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@EnableBatchIntegration
@SpringBootApplication
@EnableIntegration
public class PocSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocSpringBatchApplication.class, args);
    }

}
