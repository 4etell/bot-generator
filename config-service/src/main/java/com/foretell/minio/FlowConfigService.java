package com.foretell.minio;

import com.foretell.rabbit.client.BotConfigClient;
import com.foretell.rabbit.client.ConfigEvent;
import com.foretell.util.Pair;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Singleton
@Slf4j
public class FlowConfigService {

    @Inject
    private MinioService minioService;

    @Inject
    private BotConfigClient botConfigClient;

    @Property(name = "minio.flow-file-name")
    public String fileName;

    public void uploadFlowConfig(CompletedFileUpload file) throws IOException {
        minioService.uploadFile(file.getInputStream(), fileName);
        botConfigClient.sendMessage(ConfigEvent.FLOW_CONFIG_UPDATED);
    }

    public Pair<byte[], String> downloadFlowConfig() throws IOException {
       return Pair.of(minioService.downloadFile(fileName), fileName);
    }

    @Requires(notEnv = Environment.TEST)
    @EventListener
    public void setUpDefaultFlow(ServerStartupEvent event) {
        try {
            boolean fileExists = minioService.isFileExists(fileName);
            if (!fileExists) {
                log.info("Start upload default flow...");
                minioService.uploadFile(getClass().getClassLoader().getResourceAsStream("flow.yml"), "flow.yml");
            } else {
                log.info("Flow file exists!");
            }
        } catch (Exception e) {
            log.info("error on startup event", e);
        }
    }

}
