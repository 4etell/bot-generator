package com.foretell.flow.model.response.impl.menu.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuRowFlowModel {
    private List<MenuRowItemFlowModel> items;
}
