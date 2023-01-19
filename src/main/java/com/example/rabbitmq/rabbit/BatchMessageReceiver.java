package com.example.rabbitmq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;

@Slf4j
public class BatchMessageReceiver {

    @RabbitListener(queues = {"queue-batch"})
    public void receiveBatch(List<String> messages) {
        log.info("Received list of messages: {}", messages.size());
        for (var message : messages) {
            log.info("Mess list item: {}", message);
        }
    }

}
