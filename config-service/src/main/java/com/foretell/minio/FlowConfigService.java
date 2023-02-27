package com.foretell.minio;

import com.foretell.rabbit.client.BotConfigClient;
import com.foretell.rabbit.client.ConfigEvent;
import com.foretell.util.Pair;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
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


}
