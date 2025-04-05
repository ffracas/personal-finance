package com.example.reactive.quarkus.personal.finance.controller;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/investment")
public final class InvestmentController {
    //TODO REMOVE WHEN CREATE OTHER ENDPOINT
    @GET
    public Uni<String> dummyEndpoint() {
        return Uni.createFrom().item("investment");
    }
}
