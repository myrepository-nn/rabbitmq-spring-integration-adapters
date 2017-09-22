package com.nishant.spring.integration.nodsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegrationManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
@SpringBootApplication
@EnableIntegrationManagement
public class SpringIntegrationOutboundAdapterApplication {

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(SpringIntegrationOutboundAdapterApplication.class, args);
	}
}
