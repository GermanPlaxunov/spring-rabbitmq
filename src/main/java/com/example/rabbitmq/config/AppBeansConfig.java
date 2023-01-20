package com.example.rabbitmq.config;

import com.example.rabbitmq.receiver.ListMessageReceiver;
import com.example.rabbitmq.receiver.PersonReceiver;
import com.example.rabbitmq.receiver.SingleMessageReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeansConfig {

    private final String exchangeSingle = "exchange-single";
    private final String exchangeList = "exchange-list";
    private final String exchangePerson = "exchange-person";
    private final String bindingSingle = "bindingSingle";
    private final String bindingList = "bindingList";
    private final String bindingPerson = "bindingPerson";
    private final String routingKeySingle = "routing.single";
    private final String routingKeyList = "routing.list";
    private final String routingKeyPerson = "routing.person";
    private final String queueSingle = "queue-single";
    private final String queueList = "queue-list";
    private final String queuePerson = "queue-person";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Qualifier(queueSingle)
    public Queue queueSingle() {
        return new Queue(queueSingle, false);
    }

    @Bean
    @Qualifier(queueList)
    public Queue queueList() {
        return new Queue(queueList, false);
    }

    @Bean
    @Qualifier(queuePerson)
    public Queue queuePerson() {
        return new Queue(queuePerson, false);
    }

    @Bean
    @Qualifier(exchangeSingle)
    public TopicExchange exchangeSingle() {
        return new TopicExchange(exchangeSingle);
    }

    @Bean
    @Qualifier(exchangeList)
    public TopicExchange exchangeList() {
        return new TopicExchange(exchangeList);
    }

    @Bean
    @Qualifier(exchangePerson)
    public TopicExchange exchangePerson() {
        return new TopicExchange(exchangePerson);
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
    @Qualifier(bindingList)
    public Binding bindingList(@Qualifier(queueList) Queue queue,
                               @Qualifier(exchangeList) TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKeyList);
    }

    @Bean
    @Qualifier(bindingPerson)
    public Binding bindingPerson(@Qualifier(queuePerson) Queue queue,
                                 @Qualifier(exchangePerson) TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKeyPerson);
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
    @Qualifier("listContainer")
    public SimpleMessageListenerContainer listContainer(ConnectionFactory connectionFactory,
                                                        @Qualifier("listAdapter")
                                                        MessageListenerAdapter listenerAdapter) {
        var container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueList);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    @Qualifier("personContainer")
    public SimpleMessageListenerContainer personContainer(ConnectionFactory connectionFactory,
                                                          @Qualifier("personAdapter")
                                                          MessageListenerAdapter listenerAdapter) {
        var container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queuePerson);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    @Qualifier("singleAdapter")
    public MessageListenerAdapter singleListenerAdapter(SingleMessageReceiver singleMessageReceiver) {
        return new MessageListenerAdapter(singleMessageReceiver, "receiveMessage");
    }

    @Bean
    @Qualifier("personAdapter")
    public MessageListenerAdapter personListenerAdapter(PersonReceiver personReceiver) {
        return new MessageListenerAdapter(personReceiver, "receivePerson");
    }

    @Bean
    @Qualifier("listAdapter")
    public MessageListenerAdapter listListenerAdapter(ListMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveList");
    }

    @Bean
    public SingleMessageReceiver singleReceiver() {
        return new SingleMessageReceiver();
    }

    @Bean
    public ListMessageReceiver listMessageReceiver() {
        return new ListMessageReceiver();
    }

    @Bean
    public PersonReceiver personReceiver() {
        return new PersonReceiver();
    }
}
