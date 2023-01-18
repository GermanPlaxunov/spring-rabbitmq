package com.example.rabbitmq.mvc.controller;

import com.example.rabbitmq.rabbit.Receiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    @PostMapping(path = "/sendMessage")
    public void method(@RequestParam("message") String message) throws InterruptedException {
        rabbitTemplate.convertAndSend("spring-boot-exchange", "foo.bar.baz", message);
        receiver.getCountDownLatch().await(5000, TimeUnit.MILLISECONDS);
    }

}
