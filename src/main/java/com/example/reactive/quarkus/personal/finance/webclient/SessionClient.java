package com.example.reactive.quarkus.personal.finance.webclient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/session")
@RegisterRestClient(configKey = "session-manager")
public interface SessionClient {
    @GET
    @Path("/getExample")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<String> get();

    @POST
    @Path("/checkToken")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<String> checkToken(String token);
}