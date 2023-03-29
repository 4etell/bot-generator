package com.foretell.flow.dto.response.impl;

import com.foretell.flow.dto.response.FlowResponseDto;
import com.foretell.flow.FlowResponseType;


public record TextFlowResponseDto(String text) implements FlowResponseDto {

    @Override
    public FlowResponseType getResponseType() {
        return FlowResponseType.TEXT_RESPONSE;
    }

}
