package com.example.reactive.quarkus.personal.finance.interceptor;

import com.example.reactive.quarkus.personal.finance.webclient.SessionClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.spi.ResteasyReactiveContainerRequestContext;

@ApplicationScoped
public class HandlerPermission {
    private final SessionClient sessionClient;

    public HandlerPermission(@RestClient SessionClient sessionClient) {
        this.sessionClient = sessionClient;
    }

    @ServerRequestFilter
    public Uni<Void> filter(ResteasyReactiveContainerRequestContext requestContext) {
        String authorization = requestContext.getHeaderString("authorization");
        return sessionClient.checkToken(authorization).map(x -> {

            return x;
        }).replaceWithVoid();
    }
}
