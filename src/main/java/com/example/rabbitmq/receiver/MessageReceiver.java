package com.example.rabbitmq.receiver;

import com.example.rabbitmq.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class MessageReceiver {

    @RabbitListener(queues = "queue-single")
    public void receiveMessage(String message) {
        log.info("Received message: <{}>", message);
    }

    @RabbitListener(queues = "queue-person")
    public void receivePerson(Person person) {
        log.info("Received person: {}", person);
    }

}
