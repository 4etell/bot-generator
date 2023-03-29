package com.foretell.flow.dto;

import com.foretell.flow.dto.response.ResponseDto;

import java.util.List;


public record StateDto(String name, String command, List<ResponseDto> response, String nextStateName,
                       String variableName) {
}
