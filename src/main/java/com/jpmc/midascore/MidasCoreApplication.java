package com.jpmc.midascore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // <-- ADD THIS IMPORT
import org.springframework.web.client.RestTemplate; // <-- ADD THIS IMPORT

@SpringBootApplication
public class MidasCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidasCoreApplication.class, args);
    }

    // 👇 ADD THIS METHOD
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}