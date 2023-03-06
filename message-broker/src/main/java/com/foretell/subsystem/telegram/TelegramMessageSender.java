package com.foretell.subsystem.telegram;

import com.foretell.rabbit.listener.dto.AbstractMessageDto;
import com.foretell.subsystem.telegram.handler.TelegramMessageHandler;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Singleton
public class TelegramMessageSender {
    @Inject
    List<TelegramMessageHandler<AbstractMessageDto>> messageEntityHandlers;

    private final DefaultAbsSender bot;

    @Inject
    public TelegramMessageSender(@Property(name = "telegram.token") String telegramToken) {
        this.bot = new DefaultAbsSender(new DefaultBotOptions()) {
            @Override
            public String getBotToken() {
                return telegramToken;
            }
        };
    }

    public void send(AbstractMessageDto message) throws TelegramApiException {
        final TelegramMessageHandler<AbstractMessageDto> messageEntityHandler = messageEntityHandlers
                .stream()
                .filter(handler -> handler.getGenericParameter().equals(message.getClass()))
                .findFirst().orElseThrow(() -> new RuntimeException("Unknown MessageEntity: " + message.getClass().getSimpleName()));

        messageEntityHandler.handle(bot, message);
    }
}
