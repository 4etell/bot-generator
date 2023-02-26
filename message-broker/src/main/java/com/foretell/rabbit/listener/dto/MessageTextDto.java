package com.foretell.rabbit.listener.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.foretell.rabbit.listener.dto.MessageType.TEXT_MESSAGE;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
public class MessageTextDto extends AbstractMessageDto {
    private final String text;

    @JsonCreator
    public MessageTextDto(
            @JsonProperty("chatId") String chatId,
            @JsonProperty("text") String text) {
        super(chatId, TEXT_MESSAGE);
        this.text = text;
    }
}
