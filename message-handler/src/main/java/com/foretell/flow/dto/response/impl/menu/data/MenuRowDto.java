package com.foretell.flow.dto.response.impl.menu.data;

import lombok.Value;

import java.util.List;

@Value
public class MenuRowDto {
    List<MenuRowItemDto> items;
}
