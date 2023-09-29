package com.enigma.resttemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class RestTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestTemplateApplication.class, args);
	}

//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return builder
//				.setConnectTimeout(Duration.ofMillis(10000000))
//				.setReadTimeout(Duration.ofMillis(10000000))
//				.build();
//	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
