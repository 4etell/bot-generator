package com.foretell.flow.model.response.impl;

import com.foretell.flow.model.response.Response;
import com.foretell.flow.model.response.ResponseType;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Text response from flow configuration.
 */
@Getter
@Setter
public class TextResponseModel implements Response {

    private String text;

    @Override
    public ResponseType getResponseType() {
        return ResponseType.TEXT_RESPONSE;
    }

}
