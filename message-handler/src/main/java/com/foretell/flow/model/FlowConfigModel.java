package com.foretell.flow.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Flow config.
 */
@Getter
@Setter
public class FlowConfigModel {

    private List<StateModel> states;
}
