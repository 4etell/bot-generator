package com.foretell.rabbit.client;

import com.foretell.rabbit.client.dto.AbstractMessageDto;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("bot-message")
public interface BotMessageClient {

    @Binding("bot-message-out")
    void sendMessage(AbstractMessageDto messageDto);
}
