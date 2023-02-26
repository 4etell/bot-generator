package com.foretell.rabbit.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuRowItemDto {
    private String command;
    private String title;
    private Integer position;
}