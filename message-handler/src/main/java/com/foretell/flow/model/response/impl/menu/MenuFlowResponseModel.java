package com.foretell.flow.model.response.impl.menu;

import com.foretell.flow.model.response.impl.menu.data.MenuRowFlowModel;
import com.foretell.flow.model.response.FlowResponse;
import com.foretell.flow.FlowResponseType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Menu response from flow configuration.
 */
@Getter
@Setter
public class MenuFlowResponseModel implements FlowResponse {

    private String text;
    private List<MenuRowFlowModel> rows;
    private Boolean isStatic;

    @Override
    public FlowResponseType getResponseType() {
        return FlowResponseType.MENU_RESPONSE;
    }
}
