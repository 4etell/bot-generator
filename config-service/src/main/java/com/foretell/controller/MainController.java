package com.foretell.controller;

import com.foretell.minio.FlowConfigService;
import com.foretell.rabbit.client.BotConfigClient;
import com.foretell.util.Pair;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;

@Controller
public class MainController {

    @Inject
    private BotConfigClient botConfigClient;

    @Inject
    private FlowConfigService flowConfigService;

    @Post(consumes = {MediaType.MULTIPART_FORM_DATA}, value = "/flow-file")
    @Status(HttpStatus.OK)
    public void createFlowFile(@Part CompletedFileUpload file) throws IOException {
        flowConfigService.uploadFlowConfig(file);
    }

    @Get(value = "/flow-file/download")
    public Mono<MutableHttpResponse<byte[]>> downloadFlowFile() {
        return Mono.fromCallable(() -> {
                    Pair<byte[], String> bytesAndName = flowConfigService.downloadFlowConfig();
                    return HttpResponse.ok(bytesAndName.getLeft())
                            .header("Content-type", "application/octet-stream")
                            .header("Content-disposition", "attachment; filename=" + bytesAndName.getRight());
                }
        ).subscribeOn(Schedulers.boundedElastic());
    }
}
