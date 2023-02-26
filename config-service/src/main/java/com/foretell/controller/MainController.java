package com.foretell.controller;

import com.foretell.rabbit.client.BotConfigClient;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Status;
import jakarta.inject.Inject;

@Controller
public class MainController {

    @Inject
    private BotConfigClient botConfigClient;

    @Get
    @Status(HttpStatus.OK)
    public void get() {
        botConfigClient.sendMessage("hello");
    }
}
