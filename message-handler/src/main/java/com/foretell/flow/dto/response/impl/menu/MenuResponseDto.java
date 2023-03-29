package com.foretell.flow.dto.response.impl.menu;

import com.foretell.flow.dto.response.ResponseDto;
import com.foretell.flow.dto.response.impl.menu.data.MenuRowDto;
import com.foretell.flow.model.response.ResponseType;

import java.util.List;

public record MenuResponseDto(String text, List<MenuRowDto> rows, Boolean isStatic) implements ResponseDto {
    @Override
    public ResponseType getResponseType() {
        return ResponseType.MENU_RESPONSE;
    }
}
