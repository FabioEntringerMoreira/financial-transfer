package com.fabio.financialtransfer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Configuration class for creating and managing lock beans.
 */
@Configuration
public class LockConfig {

    /**
     * Creates a new {@link ReentrantLock} bean.
     * @return a new instance of {@link ReentrantLock}
     */
    @Bean
    public ReentrantLock reentrantLock() {
        return new ReentrantLock();
    }
}
