package com.foretell.flow.model.response.impl.menu;

import com.foretell.flow.model.response.impl.menu.data.MenuRowModel;
import com.foretell.flow.model.response.Response;
import com.foretell.flow.model.response.ResponseType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Menu response from flow configuration.
 */
@Getter
@Setter
public class MenuResponseModel implements Response {

    private String text;
    private List<MenuRowModel> rows;
    private Boolean isStatic;

    @Override
    public ResponseType getResponseType() {
        return ResponseType.MENU_RESPONSE;
    }
}
