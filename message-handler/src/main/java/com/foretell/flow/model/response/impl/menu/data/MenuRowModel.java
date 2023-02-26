package com.foretell.flow.model.response.impl.menu.data;

import com.foretell.flow.model.response.impl.menu.data.MenuRowItemModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuRowModel {
    private List<MenuRowItemModel> items;
}
