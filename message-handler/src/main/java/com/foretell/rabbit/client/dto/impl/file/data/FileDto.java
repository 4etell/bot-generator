package com.foretell.rabbit.client.dto.impl.file.data;

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
