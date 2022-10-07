package com.lshi.codulate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableWebMvc
@Configuration
public class AdminServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceApplication.class);

    @Bean("dbThreadPool")
    public ExecutorService dbThreadPool() {
        return Executors.newFixedThreadPool(3);
    }

    @Bean("jmsThreadPool")
    public ExecutorService jmsThreadPool() {
        return Executors.newFixedThreadPool(3);
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }


}
