package com.foretell.rabbit.listener;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;

@Requires(notEnv = Environment.TEST)
@RabbitListener
@Slf4j
public class BotConfigListener {

    @Queue("${config-service.queue-name}")
    public void handle(String messageDto) {
        log.info("new config msg: {}", messageDto);
    }
}
