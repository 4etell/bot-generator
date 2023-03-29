package com.foretell.rabbit.client.dto.impl.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foretell.rabbit.client.dto.AbstractMessageDto;
import com.foretell.rabbit.client.dto.impl.file.data.FileDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static com.foretell.rabbit.client.dto.MessageType.FILE_MESSAGE;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public class MessageFileDto extends AbstractMessageDto {
    private final List<FileDto> files;
    private final String text;

    @JsonCreator
    public MessageFileDto(@JsonProperty("chatId") String chatId,
                          @JsonProperty("text") String text,
                          @JsonProperty("files") List<FileDto> files) {
        super(chatId, FILE_MESSAGE);
        this.files = files;
        this.text = text;
    }
}
