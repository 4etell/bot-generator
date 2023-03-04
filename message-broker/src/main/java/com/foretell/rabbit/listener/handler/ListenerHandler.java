package com.foretell.rabbit.listener.handler;

import com.foretell.rabbit.listener.dto.AbstractMessageDto;
import io.micronaut.rabbitmq.bind.RabbitAcknowledgement;

public interface ListenerHandler {
    void handle(AbstractMessageDto messageDto, RabbitAcknowledgement acknowledgement);
}
