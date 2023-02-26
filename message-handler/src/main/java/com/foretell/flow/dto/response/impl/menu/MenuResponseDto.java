package com.foretell.flow.dto.response.impl.menu;

import com.foretell.flow.dto.response.ResponseDto;
import com.foretell.flow.dto.response.impl.menu.data.MenuRowDto;
import com.foretell.flow.model.response.ResponseType;
import lombok.Value;

import java.util.List;

@Value
public class MenuResponseDto implements ResponseDto {
    String text;
    List<MenuRowDto> rows;
    Boolean isStatic;

    @Override
    public ResponseType getResponseType() {
        return ResponseType.MENU_RESPONSE;
    }
}
