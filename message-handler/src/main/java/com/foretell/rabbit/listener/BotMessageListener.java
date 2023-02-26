package com.foretell.rabbit.listener;

import com.foretell.handler.MessageHandler;
import com.foretell.rabbit.listener.dto.UserMessageDto;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Requires(notEnv = Environment.TEST)
@RabbitListener
@Slf4j
public class BotMessageListener {

    @Inject
    private MessageHandler messageHandler;

    @Queue("bot-message-in")
    public void handle(UserMessageDto messageDto) {
        log.info("new msg: {}", messageDto);
        messageHandler.handleMessage(messageDto);
    }
}
