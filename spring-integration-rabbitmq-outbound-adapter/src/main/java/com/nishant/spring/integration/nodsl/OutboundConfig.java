package com.nishant.spring.integration.nodsl;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;


@Configuration
public class OutboundConfig {

	@Bean
	public TopicExchange topic() {
		return new TopicExchange("nntutorial.exchange");
	}

	@Bean
	public Queue autoDeleteQueue() {
		return new AnonymousQueue();
	}

	@Bean
	public Binding binding(TopicExchange topic, 
			Queue autoDeleteQueue) {
		return BindingBuilder.bind(autoDeleteQueue)
				.to(topic)
				.with("*");
	}

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container =new SimpleMessageListenerContainer(connectionFactory);
		container.setQueueNames(autoDeleteQueue().getName());
		container.setConcurrentConsumers(2);
		return container;
	}


	@Bean
	public AmqpInboundChannelAdapter inbound(SimpleMessageListenerContainer listenerContainer) {
		AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);
		adapter.setOutputChannel(outputChannel());
		return adapter;
	}

	@Bean
	public MessageChannel outputChannel() {
		return new DirectChannel();
	}

	@Bean
	public IntegrationFlow send() {
		return f -> f.channel(outputChannel()).handle(Http.outboundChannelAdapter("http://localhost:8084/sendToReceiver"));
	}

}
