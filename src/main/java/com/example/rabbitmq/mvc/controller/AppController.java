package com.example.rabbitmq.mvc.controller;

import com.example.rabbitmq.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        rabbitTemplate.convertAndSend("exchange-single", "routing.single", message);
    }

    @PostMapping(path = "/sendList")
    public void sendList(@RequestParam("listSize") Integer listSize,
                         @RequestParam("message") String message) {
        var list = new ArrayList<String>();
        for (var i = 0; i < listSize; i++) {
            list.add(message.concat(String.valueOf(i)));
        }
        rabbitTemplate.convertAndSend("exchange-list", "routing.list", list);
    }

    @PostMapping(path = "/sendPerson")
    public void sendBatch(@RequestBody Person person) {
        rabbitTemplate.convertAndSend("exchange-person", "routing.person", person);
    }
}
