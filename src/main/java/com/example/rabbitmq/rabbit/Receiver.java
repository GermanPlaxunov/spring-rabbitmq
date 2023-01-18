package com.example.rabbitmq.rabbit;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Data
@Slf4j
public class Receiver {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        log.info("Received: <{}>", message);
        countDownLatch.countDown();
    }
}
