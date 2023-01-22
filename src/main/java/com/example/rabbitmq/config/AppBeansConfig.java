package com.example.rabbitmq.config;

import com.example.rabbitmq.receiver.MessageReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeansConfig {

    private final String exchange = "main";
    private final String bindingSingle = "bindingSingle";
    private final String routingKeySingle = "foo.bar.single";
    private final String routingKeyPerson = "foo.bar.person";
    private final String queueSingle = "queue-single";
    private final String queuePerson = "queue-person";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory,
                                         MessageConverter converter) {
        var template = new RabbitTemplate(factory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    @Qualifier(queueSingle)
    public Queue queueSingle() {
        return new Queue(queueSingle, false);
    }

    @Bean
    @Qualifier(queuePerson)
    public Queue queuePerson() {
        return new Queue(queuePerson);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding bindingSingle(@Qualifier(queueSingle) Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKeySingle);
    }

    @Bean
    public Binding bindingPerson(@Qualifier(queuePerson) Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKeyPerson);
    }

    @Bean
    public SimpleMessageListenerContainer singleContainer(ConnectionFactory connectionFactory,
                                                    @Qualifier("singleAdapter")
                                                    MessageListenerAdapter adapter) {
        var container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueSingle);
        container.setMessageListener(adapter);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer personContainer(ConnectionFactory connectionFactory,
                                                          @Qualifier("personAdapter")
                                                          MessageListenerAdapter adapter) {
        var container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queuePerson);
        container.setMessageListener(adapter);
        return container;
    }

    @Bean
    @Qualifier("singleAdapter")
    public MessageListenerAdapter singleListenerAdapter(MessageReceiver messageReceiver,
                                                        MessageConverter converter) {
        var adapter = new MessageListenerAdapter(messageReceiver, "receiveMessage");
        adapter.setMessageConverter(converter);
        return adapter;
    }

    @Bean
    @Qualifier("personAdapter")
    public MessageListenerAdapter personListenerAdapter(MessageReceiver messageReceiver,
                                                        MessageConverter converter) {
        var adapter = new MessageListenerAdapter(messageReceiver, "receivePerson");
        adapter.setMessageConverter(converter);
        return adapter;
    }

    @Bean
    public MessageReceiver messageReceiver() {
        return new MessageReceiver();
    }
}
