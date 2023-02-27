package com.foretell.rabbit.listener;

import com.foretell.handler.ConfigEventHandler;
import com.foretell.rabbit.listener.dto.ConfigEvent;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Requires(notEnv = Environment.TEST)
@RabbitListener
@Slf4j
public class BotConfigListener {

    @Inject
    private ConfigEventHandler configEventHandler;

    @Queue("${config-service.queue-name}")
    public void handle(ConfigEvent event) {
        log.info("new config event: {}", event);
        configEventHandler.handleEvent(event);
    }
}
