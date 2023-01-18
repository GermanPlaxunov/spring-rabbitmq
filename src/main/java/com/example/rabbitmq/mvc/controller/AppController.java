package com.example.rabbitmq.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping(path = "/sendSingleMessage")
    public void sendSingleMessage(@RequestParam("message") String message) {
        rabbitTemplate.convertAndSend("spring-boot-exchange", "foo.bar.single", message);
    }

    @PostMapping(path = "/sendBatch")
    public void sendBatch(@RequestParam("batchSize") Integer batchSize,
                          @RequestParam("message") String message) {
        var list = new ArrayList<String>();
        for (var i = 0; i < batchSize; i++) {
            list.add(message);
        }
        rabbitTemplate.convertAndSend("spring-boot-exchange", "foo.bar.batch", list);
    }

}
