package com.foretell.rabbit.listener;

import com.foretell.rabbit.listener.dto.AbstractMessageDto;
import com.foretell.rabbit.listener.handler.ListenerHandler;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.rabbitmq.bind.RabbitAcknowledgement;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Requires(notEnv = Environment.TEST)
@RabbitListener
@Slf4j
public class BotMessageListener {

    @Inject
    private ListenerHandler listenerHandler;

    @Queue("bot-message-out")
    public void handle(AbstractMessageDto messageDto, RabbitAcknowledgement acknowledgement) {
        listenerHandler.handle(messageDto, acknowledgement);
    }

}

