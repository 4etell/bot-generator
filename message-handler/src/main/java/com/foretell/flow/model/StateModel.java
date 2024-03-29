package com.foretell.flow.model;

import com.foretell.flow.model.response.FlowResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type State from flow configuration.
 */
@Getter
@Setter
public class StateModel {

    private String name;

    private String command;

    private List<FlowResponse> flowResponse;

    private String nextStateName;

    private String variableName;

}
