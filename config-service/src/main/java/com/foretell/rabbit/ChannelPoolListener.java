package com.foretell.rabbit;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class ChannelPoolListener extends ChannelInitializer {
    @Override
    public void initialize(Channel channel, String name) throws IOException {
        channel.exchangeDeclare("bot-config", BuiltinExchangeType.FANOUT, true);
    }
}
