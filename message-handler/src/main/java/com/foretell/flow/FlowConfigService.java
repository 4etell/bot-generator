package com.foretell.flow;

import com.foretell.flow.dto.FlowConfigDto;
import com.foretell.flow.mapper.FlowConfigMapper;
import com.foretell.flow.model.FlowConfigModel;
import com.foretell.flow.model.StateModel;
import com.foretell.flow.model.response.Response;
import com.foretell.flow.model.response.impl.TextResponseModel;
import com.foretell.flow.model.response.impl.menu.MenuResponseModel;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.InputStream;

@Singleton
public class FlowConfigService {

    @Inject
    private FlowConfigMapper flowConfigMapper;

    public FlowConfigDto getFlowConfig() {
        Constructor constructor = new Constructor(FlowConfigModel.class);
        constructor.addTypeDescription(new TypeDescription(StateModel.class));
        constructor.addTypeDescription(new TypeDescription(Response.class));

        constructor.addTypeDescription(new TypeDescription(TextResponseModel.class, new Tag("!text")));
        constructor.addTypeDescription(new TypeDescription(MenuResponseModel.class, new Tag("!menu")));

        Yaml yaml = new Yaml(constructor);
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("flow.yml");

        FlowConfigModel flowConfig = yaml.load(inputStream);
        return flowConfigMapper.mapFlowConfigModelToFlowConfigDto(flowConfig);
    }
}
