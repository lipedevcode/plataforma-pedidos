package com.pedidosplataforma.reservarestoque.config;

import com.google.gson.Gson;
import com.pedidosplataforma.reservarestoque.component.ReservarEstoqueReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class ReservarEstoqueConfig {
    @Value("${pedidos.queue.name}")
    private String queueName;

    @Value("${pedidos.exchange.name}")
    private String exchangeName;

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(ReservarEstoqueReceiver reservarEstoqueReceiver) {
        return new MessageListenerAdapter(reservarEstoqueReceiver, "receiveMessage");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }
}
