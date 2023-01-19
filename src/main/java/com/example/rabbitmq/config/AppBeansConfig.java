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

    private final String exchangeSingle = "exchange-single";
    private final String exchangeBatch = "exchange-batch";
    private final String bindingSingle = "bindingSingle";
    private final String bindingBatch = "bindingBatch";
    private final String routingKeySingle = "routing.single";
    private final String routingKeyBatch = "routing.batch";
    private final String queueSingle = "queue-single";
    private final String queueBatch = "queue-batch";

    @Bean
    @Qualifier(queueSingle)
    public Queue queueSingle() {
        return new Queue(queueSingle, false);
    }

    @Bean
    @Qualifier(queueBatch)
    public Queue queueBatch() {
        return new Queue(queueBatch, false);
    }

    @Bean
    @Qualifier(exchangeSingle)
    public TopicExchange exchangeSingle() {
        return new TopicExchange(exchangeSingle);
    }

    @Bean
    @Qualifier(exchangeBatch)
    public TopicExchange exchangeBatch() {
        return new TopicExchange(exchangeBatch);
    }

    @Bean
    @Qualifier(bindingSingle)
    public Binding bindingSingle(@Qualifier(queueSingle) Queue queue,
                                 @Qualifier(exchangeSingle) TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKeySingle);
    }

    @Bean
    @Qualifier(bindingBatch)
    public Binding bindingBatch(@Qualifier(queueBatch) Queue queue,
                                @Qualifier(exchangeBatch) TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKeyBatch);
    }

    @Bean
    @Qualifier("singleContainer")
    public SimpleMessageListenerContainer singleContainer(ConnectionFactory connectionFactory,
                                                    @Qualifier("singleAdapter")
                                                    MessageListenerAdapter listenerAdapter) {
        var container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueSingle);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    @Qualifier("batchContainer")
    public SimpleMessageListenerContainer batchContainer(ConnectionFactory connectionFactory,
                                                    @Qualifier("batchAdapter")
                                                    MessageListenerAdapter listenerAdapter) {
        var container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueBatch);
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
