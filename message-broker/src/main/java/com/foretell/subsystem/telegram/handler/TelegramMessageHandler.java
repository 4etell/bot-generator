package com.foretell.subsystem.telegram.handler;

import com.foretell.rabbit.listener.dto.AbstractMessageDto;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramMessageHandler<T extends AbstractMessageDto> {
    void handle(DefaultAbsSender bot, T message) throws TelegramApiException;

    Class<T> getGenericParameter();

    default SendMessage getBasicSendMessage(AbstractMessageDto messageEntity) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode("HTML");
        sendMessage.setChatId(messageEntity.getChatId());
        return sendMessage;
    }
}
