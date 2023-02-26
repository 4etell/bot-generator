package com.foretell.flow.dto;

import lombok.Value;

import java.util.List;

@Value
public class FlowConfigDto {
    List<StateDto> states;
}
