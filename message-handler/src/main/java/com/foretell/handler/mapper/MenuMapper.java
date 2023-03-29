package com.foretell.handler.mapper;

import com.foretell.flow.dto.response.impl.menu.data.MenuRowFlowDto;
import com.foretell.rabbit.client.dto.impl.menu.data.MenuRowDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface MenuMapper {

    MenuRowDto menuRowToMenuRowDto(MenuRowFlowDto menuRowItem);

}
