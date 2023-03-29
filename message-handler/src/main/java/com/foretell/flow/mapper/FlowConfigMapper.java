package com.foretell.flow.mapper;

import com.foretell.flow.dto.FlowConfigDto;
import com.foretell.flow.dto.response.FlowResponseDto;
import com.foretell.flow.dto.response.impl.TextFlowResponseDto;
import com.foretell.flow.dto.response.impl.menu.MenuFlowResponseDto;
import com.foretell.flow.model.FlowConfigModel;
import com.foretell.flow.model.response.FlowResponse;
import com.foretell.flow.FlowResponseType;
import com.foretell.flow.model.response.impl.TextFlowResponseModel;
import com.foretell.flow.model.response.impl.menu.MenuFlowResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface FlowConfigMapper {

    FlowConfigDto mapFlowConfigModelToFlowConfigDto(FlowConfigModel flowConfigModel);

    TextFlowResponseDto mapTextResponseToTextResponseDto(TextFlowResponseModel textResponseModel);

    MenuFlowResponseDto mapMenuResponseToMenuResponseDto(MenuFlowResponseModel menuResponseModel);

    default FlowResponseDto mapResponseToResponseDto(FlowResponse flowResponse) {
        // implementation
        FlowResponseType flowResponseType = flowResponse.getResponseType();
        switch (flowResponseType) {
            case TEXT_RESPONSE -> {
                return mapTextResponseToTextResponseDto((TextFlowResponseModel) flowResponse);
            }
            case MENU_RESPONSE -> {
                return mapMenuResponseToMenuResponseDto((MenuFlowResponseModel) flowResponse);
            }
            default -> throw new IllegalArgumentException(String.format("Cannot recognize responseType %s", flowResponseType));
        }
    }
}
