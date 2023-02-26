package com.foretell.rabbit.listener.dto;

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
