package com.nishant.spring.integration.nodsl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

@Configuration
public class InboundConfig {

	@Bean
	public IntegrationFlow received() {
		return IntegrationFlows.from(Http.inboundChannelAdapter("/receivedMsg")
				.requestMapping(arg0 -> arg0.methods(HttpMethod.POST))
				.requestPayloadType(String.class)
				)
				.channel(receivedInAdapter())
				.get();
	}

	@Bean
	public MessageChannel receivedInAdapter() {
		return new DirectChannel();
	}

	@Bean
	public TopicExchange topic() {
		return new TopicExchange("nntutorial.exchange");
	}

	@Bean
	@ServiceActivator(inputChannel = "receivedInAdapter")
	public AmqpOutboundEndpoint amqpOutbound(AmqpTemplate amqpTemplate) {
		AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate);
		outbound.setRoutingKey("key");
		outbound.setExchangeName("nntutorial.exchange");
		return outbound;
	}

}
