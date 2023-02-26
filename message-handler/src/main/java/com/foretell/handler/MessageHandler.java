package com.foretell.handler;

import com.foretell.rabbit.client.BotMessageClient;
import com.foretell.rabbit.client.dto.MessageTextDto;
import com.foretell.rabbit.listener.dto.UserMessageDto;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class MessageHandler {

    @Inject
    private BotMessageClient botMessageClient;

    public void handleMessage(UserMessageDto userMessageDto) {
        botMessageClient.sendMessage(new MessageTextDto(userMessageDto.getChatId().toString(), userMessageDto.getText()));
    }
}
