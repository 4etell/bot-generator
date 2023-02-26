package com.foretell.flow.dto;

import com.foretell.flow.dto.response.ResponseDto;
import lombok.Value;

import java.util.List;

@Value
public class StateDto {
    String name;
    String command;
    List<ResponseDto> response;
    String nextStateName;
    String variableName;
}
