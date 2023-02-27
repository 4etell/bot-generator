package com.foretell.handler;

import com.foretell.flow.StateService;
import com.foretell.rabbit.listener.dto.ConfigEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ConfigEventHandler {

    @Inject
    private StateService stateService;

    public void handleEvent(ConfigEvent event) {
        switch (event) {
            case FLOW_CONFIG_UPDATED -> stateService.updateConfig();
        }
    }
}
