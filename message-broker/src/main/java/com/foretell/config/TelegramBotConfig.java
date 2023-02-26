package com.foretell.config;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class TelegramBotConfig {

    public String getTelegramBotToken() {
        return System.getenv("TELEGRAM_BOT_TOKEN");
    }
}
