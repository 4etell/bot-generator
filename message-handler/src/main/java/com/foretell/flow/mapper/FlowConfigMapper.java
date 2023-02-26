package com.foretell.flow.mapper;

import com.foretell.flow.dto.FlowConfigDto;
import com.foretell.flow.dto.response.ResponseDto;
import com.foretell.flow.dto.response.impl.TextResponseDto;
import com.foretell.flow.dto.response.impl.menu.MenuResponseDto;
import com.foretell.flow.model.FlowConfigModel;
import com.foretell.flow.model.response.Response;
import com.foretell.flow.model.response.ResponseType;
import com.foretell.flow.model.response.impl.TextResponseModel;
import com.foretell.flow.model.response.impl.menu.MenuResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface FlowConfigMapper {

    FlowConfigDto mapFlowConfigModelToFlowConfigDto(FlowConfigModel flowConfigModel);

    TextResponseDto mapTextResponseToTextResponseDto(TextResponseModel textResponseModel);

    MenuResponseDto mapMenuResponseToMenuResponseDto(MenuResponseModel menuResponseModel);

    default ResponseDto mapResponseToResponseDto(Response response) {
        // implementation
        ResponseType responseType = response.getResponseType();
        switch (responseType) {
            case TEXT_RESPONSE -> {
                return mapTextResponseToTextResponseDto((TextResponseModel) response);
            }
            case MENU_RESPONSE -> {
                return mapMenuResponseToMenuResponseDto((MenuResponseModel) response);
            }
            default -> throw new IllegalArgumentException(String.format("Cannot recognize responseType %s", responseType));
        }
    }
}
