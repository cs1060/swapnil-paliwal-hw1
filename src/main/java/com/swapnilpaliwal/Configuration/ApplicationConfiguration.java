package com.swapnilpaliwal.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Random;


@Configuration
public class ApplicationConfiguration {

    // Use this to make any external API call
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Use this to generate random numbers
    @Bean
    Random random() {
        return new Random();
    }

}
