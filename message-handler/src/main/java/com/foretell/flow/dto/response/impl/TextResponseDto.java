package com.foretell.flow.dto.response.impl;

import com.foretell.flow.dto.response.ResponseDto;
import com.foretell.flow.model.response.ResponseType;


public record TextResponseDto(String text) implements ResponseDto {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.TEXT_RESPONSE;
    }

}
