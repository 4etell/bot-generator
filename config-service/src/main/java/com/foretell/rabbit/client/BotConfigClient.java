package com.foretell.rabbit.client;

import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("bot-config")
public interface BotConfigClient {

    @Binding(value = "bot-config-event")
    void sendMessage(ConfigEvent event);
}
