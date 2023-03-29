package com.foretell.rabbit.client.dto.impl.menu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foretell.rabbit.client.dto.AbstractMessageDto;
import com.foretell.rabbit.client.dto.impl.menu.data.MenuRowDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static com.foretell.rabbit.client.dto.MessageType.MENU_MESSAGE;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class MessageMenuDto extends AbstractMessageDto {
    private final String text;
    private final List<MenuRowDto> rows;

    @JsonCreator
    public MessageMenuDto(@JsonProperty("chatId") String chatId,
                          @JsonProperty("text") String text,
                          @JsonProperty("rows") List<MenuRowDto> rows) {
        super(chatId, MENU_MESSAGE);
        this.text = text;
        this.rows = rows;
    }
}
