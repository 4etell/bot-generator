package com.foretell.subsystem.telegram.handler;

import com.foretell.rabbit.listener.dto.MessageTextDto;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Singleton
public class TelegramMessageTextHandler implements TelegramMessageHandler<MessageTextDto> {
    @Override
    public void handle(DefaultAbsSender bot, MessageTextDto message) throws TelegramApiException {
        SendMessage sendMessage = getBasicSendMessage(message);
        sendMessage.setText(message.getText());
        sendMessage.setParseMode("HTML");
        bot.execute(sendMessage);
    }

    @Override
    public Class<MessageTextDto> getGenericParameter() {
        return MessageTextDto.class;
    }
}
