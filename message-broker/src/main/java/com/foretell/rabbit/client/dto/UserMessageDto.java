package com.foretell.rabbit.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageDto {
    private String command;
    private Long chatId;
    private Long timeStamp;
    private String text;
}
