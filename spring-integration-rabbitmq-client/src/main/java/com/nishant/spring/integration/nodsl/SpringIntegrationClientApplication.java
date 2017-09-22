package com.nishant.spring.integration.nodsl;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;

import com.fasterxml.jackson.core.JsonProcessingException;
@SpringBootApplication
@IntegrationComponentScan
@EnableIntegration
public class SpringIntegrationClientApplication {

	public static void main(String[] args) throws JsonProcessingException {
		 ConfigurableApplicationContext cxt=SpringApplication.run(SpringIntegrationClientApplication.class, args);
		 Sender sender=cxt.getBean(Sender.class);
		 Scanner scanner=new Scanner(System.in);
		 while(scanner.hasNext()) {
			 String msgtosend=scanner.next();
			 sender.send(msgtosend);
		 }
	}

	
	@MessagingGateway(defaultRequestChannel="sendToRabbitMQ.input")
	public interface Sender{
		public void send(String msg);
	}
	
	@Bean
	public IntegrationFlow sendToRabbitMQ() {
		return s -> s.handle(Http.outboundChannelAdapter("http://localhost:8082/receivedMsg"));
	}
}
