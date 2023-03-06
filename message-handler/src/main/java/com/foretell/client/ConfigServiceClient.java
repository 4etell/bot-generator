package com.foretell.client;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
@Requires(notEnv = Environment.TEST)
public class ConfigServiceClient {
    private final HttpClient httpClient;

    @Property(name = "config-service.username")
    private String username;
    @Property(name = "config-service.password")
    private String password;

    public ConfigServiceClient(@Client("${config-service.client-url}") HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Mono<byte[]> getFlowInputStream() {
        HttpRequest<?> request = HttpRequest.GET(UriBuilder.of("/flow-file/download").build()).basicAuth(username, password);
        return Mono.from(httpClient.retrieve(request, byte[].class));
    }   
}
