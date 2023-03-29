package com.foretell.flow.dto;

import com.foretell.flow.dto.response.FlowResponseDto;

import java.util.List;


public record StateDto(String name, String command, List<FlowResponseDto> response, String nextStateName,
                       String variableName) {
}
