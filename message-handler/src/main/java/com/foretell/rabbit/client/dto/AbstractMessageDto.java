package com.foretell.rabbit.client.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "messageType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MessageTextDto.class, name = "TEXT_MESSAGE"),
        @JsonSubTypes.Type(value = MessageMenuDto.class, name = "MENU_MESSAGE"),
        @JsonSubTypes.Type(value = MessageFileDto.class, name = "FILE_MESSAGE")
})
public abstract class AbstractMessageDto {
    private final String chatId;
    private final MessageType messageType;

    protected AbstractMessageDto(String chatId, MessageType messageType) {
        this.chatId = chatId;
        this.messageType = messageType;
    }
}
