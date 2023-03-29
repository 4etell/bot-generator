package com.foretell.flow;

import com.foretell.flow.dto.FlowConfigDto;
import com.foretell.flow.mapper.FlowConfigMapper;
import com.foretell.flow.model.FlowConfigModel;
import com.foretell.flow.model.StateModel;
import com.foretell.flow.model.response.FlowResponse;
import com.foretell.flow.model.response.impl.TextFlowResponseModel;
import com.foretell.flow.model.response.impl.menu.MenuFlowResponseModel;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Singleton
@Slf4j
public class FlowConfigService {

    @Inject
    private FlowConfigMapper flowConfigMapper;

    public FlowConfigDto getFlowConfig(byte[] bytes) {
        Constructor constructor = new Constructor(FlowConfigModel.class);
        constructor.addTypeDescription(new TypeDescription(StateModel.class));
        constructor.addTypeDescription(new TypeDescription(FlowResponse.class));

        constructor.addTypeDescription(new TypeDescription(TextFlowResponseModel.class, new Tag("!text")));
        constructor.addTypeDescription(new TypeDescription(MenuFlowResponseModel.class, new Tag("!menu")));

        Yaml yaml = new Yaml(constructor);

        try (InputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            FlowConfigModel flowConfig = yaml.load(byteArrayInputStream);
            return flowConfigMapper.mapFlowConfigModelToFlowConfigDto(flowConfig);
        } catch (IOException e) {
            log.error("Error during get flow config", e);
            throw new RuntimeException(e);
        }
    }
}
