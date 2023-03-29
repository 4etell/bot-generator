package com.foretell.flow.model.response.impl;

import com.foretell.flow.model.response.FlowResponse;
import com.foretell.flow.FlowResponseType;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Text response from flow configuration.
 */
@Getter
@Setter
public class TextFlowResponseModel implements FlowResponse {

    private String text;

    @Override
    public FlowResponseType getResponseType() {
        return FlowResponseType.TEXT_RESPONSE;
    }

}
