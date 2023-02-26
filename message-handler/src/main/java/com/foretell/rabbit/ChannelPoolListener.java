package com.foretell.rabbit;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import io.micronaut.context.annotation.Property;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class ChannelPoolListener extends ChannelInitializer {

    @Property(name = "config-service.queue-name")
    private String queueName;

    @Override
    public void initialize(Channel channel, String name) throws IOException {
        channel.exchangeDeclare("bot-message", BuiltinExchangeType.DIRECT, true);
        channel.queueDeclare("bot-message-in", true, false, false, null);
        channel.queueBind("bot-message-in", "bot-message", "bot-message-in");
        channel.queueDeclare("bot-message-out", true, false, false, null);
        channel.queueBind("bot-message-out", "bot-message", "bot-message-out");

        channel.queueDeclare(queueName, true, true, false, null);
        channel.queueBind(queueName, "bot-config", "bot-config-event");
    }
}
