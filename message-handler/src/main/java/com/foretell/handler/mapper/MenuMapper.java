package com.foretell.handler.mapper;

import com.foretell.rabbit.client.dto.MenuRowDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface MenuMapper {

    MenuRowDto menuRowToMenuRowDto(com.foretell.flow.dto.response.impl.menu.data.MenuRowDto menuRowItem);

}
