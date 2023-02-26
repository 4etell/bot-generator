package com.foretell.flow.dto.response.impl;

import com.foretell.flow.dto.response.ResponseDto;
import com.foretell.flow.model.response.ResponseType;
import lombok.Value;

@Value
public class TextResponseDto implements ResponseDto {

    String text;

    @Override
    public ResponseType getResponseType() {
        return ResponseType.TEXT_RESPONSE;
    }

}
