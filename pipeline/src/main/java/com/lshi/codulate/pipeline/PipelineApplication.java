package com.lshi.codulate.pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableWebMvc
@Configuration
public class PipelineApplication {

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean("ioThreadPool")
	public ExecutorService ioThreadPool() {
		return Executors.newFixedThreadPool(3);
	}

	@Bean("jmsThreadPool")
	public ExecutorService jmsThreadPool() {
		return Executors.newFixedThreadPool(3);
	}

	public static void main(String[] args) {
		SpringApplication.run(PipelineApplication.class, args);
	}

}
