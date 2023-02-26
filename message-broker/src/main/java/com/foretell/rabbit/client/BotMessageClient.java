package com.foretell.rabbit.client;

import com.foretell.rabbit.client.dto.UserMessageDto;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("bot-message")
public interface BotMessageClient {

    @Binding("bot-message-in")
    void sendMessage(UserMessageDto messageDto);
}
