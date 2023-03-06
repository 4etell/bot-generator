package com.foretell.controller;

import com.foretell.minio.FlowConfigService;
import com.foretell.rabbit.client.BotConfigClient;
import com.foretell.util.Pair;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ServiceController {
    @Inject
    private FlowConfigService flowConfigService;

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
