package com.foretell.controller;

import com.foretell.rabbit.client.BotMessageClient;
import com.foretell.rabbit.client.dto.UserMessageDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller("/")
public class Main {

    @Inject
    private BotMessageClient rabbitClient;

    @Get
    public String lol() {
        rabbitClient.sendMessage(new UserMessageDto("/hello", 1L, 1L, "some text"));
        return "hello";
    }
}
