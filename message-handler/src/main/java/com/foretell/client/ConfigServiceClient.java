package com.foretell.client;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class ConfigServiceClient {
    private final HttpClient httpClient;

    public ConfigServiceClient(@Client("${config-service.client-url}") HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Mono<byte[]> getFlowInputStream() {
        HttpRequest<?> request = HttpRequest.GET(UriBuilder.of("/file/download?name=flow.yml").build());

        return Mono.from(httpClient.retrieve(request, byte[].class));
    }   
}
