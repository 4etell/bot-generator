package com.foretell.rabbit.listener.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static com.foretell.rabbit.listener.dto.MessageType.MENU_MESSAGE;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class MessageMenuDto extends AbstractMessageDto {
    private final String text;
    private final List<MenuRowDto> rows;

    public MessageMenuDto(String chatId, String text, List<MenuRowDto> rows) {
        super(chatId, MENU_MESSAGE);
        this.text = text;
        this.rows = rows;
    }
}
