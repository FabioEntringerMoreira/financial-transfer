package com.fabio.financialtransfer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for creating and managing RestTemplate beans.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a new {@link RestTemplate} bean.
     * @return a new instance of {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
