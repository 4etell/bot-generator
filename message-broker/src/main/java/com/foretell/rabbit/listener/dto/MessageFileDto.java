package com.foretell.rabbit.listener.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static com.foretell.rabbit.listener.dto.MessageType.FILE_MESSAGE;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class MessageFileDto extends AbstractMessageDto {
    private final List<FileDto> files;
    private final String text;

    public MessageFileDto(String chatId, List<FileDto> files, String text) {
        super(chatId, FILE_MESSAGE);
        this.files = files;
        this.text = text;
    }
}
