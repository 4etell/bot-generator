package com.foretell.flow;

import com.foretell.flow.dto.FlowConfigDto;
import com.foretell.flow.dto.StateDto;
import com.foretell.flow.exception.StateServiceException;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Singleton
@Slf4j
public class StateService {

    private final FlowConfigService flowConfigService;
    private final AtomicReference<FlowConfigDto> flowConfig;

    @Inject
    public StateService(FlowConfigService flowConfigService) {
        this.flowConfigService = flowConfigService;
        flowConfig = new AtomicReference<>(flowConfigService.getFlowConfig());
    }

    public void updateConfig() {
        flowConfig.set(flowConfigService.getFlowConfig());
    }


    public Mono<StateDto> getStateByCommand(String messageCommand) {
        return Mono.fromCallable(() -> {
            if (messageCommand == null) {
                String errorText = "Given messageCommand is null!";
                throw new StateServiceException(errorText);
            }
            String messagePath = getPath(messageCommand);
            return getStateByPredicate(s -> commandEquals(messagePath, s.getCommand())).orElseThrow(() -> new StateServiceException("Cannot find state by command: " + messageCommand));
        });
    }

    public StateDto getStateByName(String stateName) {
        if (stateName == null) {
            throw new StateServiceException("Given stateName is null!");
        }
        return getStateByPredicate(s -> stateName.equals(s.getName()))
                .orElseThrow(() -> new StateServiceException(String.format("Cannot find state by name: %s", stateName)));
    }

    public String getPath(String command) {
        if (command.split("\\s")[0].equals("/start")) {
            return "/start";
        }
        String formattedCommand = command.replaceAll("\\s+", "");
        if (formattedCommand.contains("?")) {
            return formattedCommand.substring(0, command.indexOf("?"));
        }
        return formattedCommand;
    }

    public Map<String, String> getCommandParams(@NonNull String command) {
        URI uri = URI.create(command.replaceAll("\\s+", ""));
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = uri.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                queryPairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
            }
        }
        return queryPairs;
    }

    private Optional<StateDto> getStateByPredicate(Predicate<StateDto> predicate) {
        List<StateDto> stateDtoList =
                flowConfig
                        .get()
                        .getStates()
                        .stream()
                        .filter(predicate)
                        .toList();

        if (stateDtoList.size() == 1) {
            return Optional.of(stateDtoList.get(0));
        } else {
            return Optional.empty();
        }
    }

    private boolean commandEquals(String messagePath, String configCommand) {
        if (configCommand != null) {
            return messagePath.equals(getPath(configCommand));
        }
        return false;
    }
}
