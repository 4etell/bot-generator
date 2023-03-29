package com.foretell.flow.dto.response.impl.menu;

import com.foretell.flow.dto.response.FlowResponseDto;
import com.foretell.flow.dto.response.impl.menu.data.MenuRowFlowDto;
import com.foretell.flow.FlowResponseType;

import java.util.List;

public record MenuFlowResponseDto(String text, List<MenuRowFlowDto> rows, Boolean isStatic) implements FlowResponseDto {
    @Override
    public FlowResponseType getResponseType() {
        return FlowResponseType.MENU_RESPONSE;
    }
}
