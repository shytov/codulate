package com.lshi.codulate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableWebMvc
@Configuration
public class AdminServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceApplication.class);

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean("ioThreadPool")
    public ExecutorService ioThreadPool() {
        return Executors.newFixedThreadPool(3);
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }


}
