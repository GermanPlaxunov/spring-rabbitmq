package com.example.rabbitmq.config;

import com.example.rabbitmq.rabbit.BatchMessageReceiver;
import com.example.rabbitmq.rabbit.SingleMessageReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeansConfig {

    private final String EXCHANGE_SINGLE = "exchange-single";
    private final String EXCHANGE_BATCH = "exchange-batch";
    private final String BINDING_SINGLE = "bindingSingle";
    private final String BINDING_BATCH = "bindingBatch";
    private final String ROUTING_KEY_SINGLE = "routing.single";
    private final String ROUTING_KEY_BATCH = "routing.batch";
    private final String queueName1 = "queue-1";
    private final String queueName2 = "queue-2";

    @Bean
    @Qualifier(queueName1)
    public Queue queue1() {
        return new Queue(queueName1, false);
    }

    @Bean
    @Qualifier(queueName2)
    public Queue queue2() {
        return new Queue(queueName2, false);
    }

    @Bean
    @Qualifier(EXCHANGE_SINGLE)
    public TopicExchange exchangeSingle() {
        return new TopicExchange(EXCHANGE_SINGLE);
    }

    @Bean
    @Qualifier(EXCHANGE_BATCH)
    public TopicExchange exchangeBatch() {
        return new TopicExchange(EXCHANGE_BATCH);
    }

    @Bean
    @Qualifier(BINDING_SINGLE)
    public Binding bindingSingle(@Qualifier(queueName1) Queue queue,
                                 @Qualifier(EXCHANGE_SINGLE) TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ROUTING_KEY_SINGLE);
    }

    @Bean
    @Qualifier(BINDING_BATCH)
    public Binding binding(@Qualifier(queueName2) Queue queue,
                           @Qualifier(EXCHANGE_BATCH) TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ROUTING_KEY_BATCH);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    @Qualifier("batchAdapter")
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName1, queueName2);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    @Qualifier("singleAdapter")
    public MessageListenerAdapter singleListenerAdapter(SingleMessageReceiver singleMessageReceiver) {
        return new MessageListenerAdapter(singleMessageReceiver, "receiveMessage");
    }

    @Bean
    @Qualifier("batchAdapter")
    public MessageListenerAdapter batchListenerAdapter(BatchMessageReceiver batchMessageReceiver) {
        return new MessageListenerAdapter(batchMessageReceiver, "receiveBatch");
    }

    @Bean
    public SingleMessageReceiver singleReceiver() {
        return new SingleMessageReceiver();
    }

    @Bean
    public BatchMessageReceiver batchReceiver() {
        return new BatchMessageReceiver();
    }
}
