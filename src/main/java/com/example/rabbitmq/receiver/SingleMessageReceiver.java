package com.example.rabbitmq.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class SingleMessageReceiver {

    @RabbitListener(queues = {"queue-single"})
    public void receiveMessage(String message) {
        log.info("Received message: <{}>", message);
    }
}
