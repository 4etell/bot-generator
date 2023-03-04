package com.foretell.rabbit.listener.handler.telegram;


import com.foretell.rabbit.listener.dto.AbstractMessageDto;
import com.foretell.rabbit.listener.handler.ListenerHandler;
import com.foretell.subsystem.telegram.TelegramMessageSender;
import io.micronaut.rabbitmq.bind.RabbitAcknowledgement;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.concurrent.TimeUnit;


@Singleton
@Slf4j
public class TelegramListenerHandler implements ListenerHandler {

    @Inject
    private TelegramMessageSender telegramMessageSender;

    private volatile boolean listening = true;

    public void handle(AbstractMessageDto messageDto, RabbitAcknowledgement acknowledgement) {
        for (; ; ) {
            try {
                if (listening) {
                    log.info("new msg {}", messageDto);
                    telegramMessageSender.send(messageDto);
                } else {
                    continue;
                }
            } catch (TelegramApiRequestException telegramApiRequestException) {
                if (telegramApiRequestException.getErrorCode() == 429) {
                    log.warn("Error [429] during send message to telegram, too many request");
                    try {
                        // заставляем всех обработчиков ждать 2 секунды, если слишком много реквестов от телеграма
                        listening = false;
                        TimeUnit.SECONDS.sleep(2); // ждем 2 секунды
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt(); // сохраняем флаг прерывания
                        log.warn("Interrupted while waiting, listener will continue to consume messages");
                    } finally {
                        listening = true;
                    }
                    // выходим и отправляем сообщение на reQueue
                    acknowledgement.nack(false, true);
                    return;
                }
                log.error("Error during send telegram message, message will be marked as read. Problem message: {}", messageDto, telegramApiRequestException);
            } catch (Exception e) {
                log.error("Error during send telegram message, message will be marked as read. Problem message: {}", messageDto, e);
            }
            // выходим и помечаем сообщение прочитанным
            acknowledgement.ack();
            return;
        }
    }
}
