package com.foretell.rabbit.listener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    // в дальнейшем fileId в FileService
    private String fileLink;
    @JsonProperty
    private boolean isPicture;
}
