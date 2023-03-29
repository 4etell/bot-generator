package com.foretell.rabbit.client.dto.impl.menu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuRowDto {
    private List<MenuRowItemDto> items;
}
