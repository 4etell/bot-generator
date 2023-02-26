package com.foretell.controller;

import com.foretell.minio.MinioService;
import com.foretell.rabbit.client.BotConfigClient;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
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
    private MinioService minioService;

    @Get
    @Status(HttpStatus.OK)
    public void get() {
        botConfigClient.sendMessage("hello");
    }

    @Post(consumes = {MediaType.MULTIPART_FORM_DATA}, value = "/file")
    @Status(HttpStatus.OK)
    public void createFaqFile(@Part CompletedFileUpload file) throws IOException {
        minioService.uploadFile(file);
    }

    @Get(value = "/file/download")
    public Mono<MutableHttpResponse<byte[]>> downLoadFile(@QueryValue String name) throws IOException {
       return Mono.fromCallable(() -> {
           byte[] bytes = minioService.downloadFile(name);
           return HttpResponse.ok(bytes)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=" + name);
       }
        ).subscribeOn(Schedulers.boundedElastic());
    }
}
