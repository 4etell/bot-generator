package com.foretell.flow;

import com.foretell.flow.dto.FlowConfigDto;
import com.foretell.flow.mapper.FlowConfigMapper;
import com.foretell.flow.model.FlowConfigModel;
import com.foretell.flow.model.StateModel;
import com.foretell.flow.model.response.Response;
import com.foretell.flow.model.response.impl.TextResponseModel;
import com.foretell.flow.model.response.impl.menu.MenuResponseModel;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        constructor.addTypeDescription(new TypeDescription(Response.class));

        constructor.addTypeDescription(new TypeDescription(TextResponseModel.class, new Tag("!text")));
        constructor.addTypeDescription(new TypeDescription(MenuResponseModel.class, new Tag("!menu")));

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
