package com.example.rabbitmq.receiver;

import com.example.rabbitmq.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Slf4j
public class PersonReceiver {

    @RabbitListener(replyContentType = "application/json",
            queues = {"queue-person"}, messageConverter = "messageConverter")
    public void receivePerson(Byte[] person) {
        log.info("Person: {}", person);
    }
}
