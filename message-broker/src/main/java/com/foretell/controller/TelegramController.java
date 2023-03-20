package com.foretell.controller;

import com.foretell.rabbit.client.BotMessageClient;
import com.foretell.rabbit.client.dto.UserMessageDto;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Inject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller("/telegram")
public class TelegramController {
    @Inject
    private BotMessageClient botMessageClient;

    @Post
    @Async
    @Status(HttpStatus.OK)
    public void webhook(@Body Update update) {
        botMessageClient.sendMessage(
                update.getCallbackQuery() == null
                        ?
                        convertTelegramMessageToUserMessageDto(update.getMessage())
                        :
                        convertCallbackQueryToUserMessageDto(update.getCallbackQuery())
        );
    }

    private UserMessageDto convertCallbackQueryToUserMessageDto(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        return new UserMessageDto(
                callbackQuery.getData(),
                message.getChatId(),
                message.getDate().longValue(),
                message.getText()
        );
    }

    private UserMessageDto convertTelegramMessageToUserMessageDto(Message message) {
        return new UserMessageDto(
                message.getText().charAt(0) == '/' ? message.getText() : null,
                message.getChatId(),
                message.getDate().longValue(),
                message.getText());
    }
}
