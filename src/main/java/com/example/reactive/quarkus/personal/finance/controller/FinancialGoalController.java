package com.example.reactive.quarkus.personal.finance.controller;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/financial-goal")
public final class FinancialGoalController {
    //TODO REMOVE WHEN CREATE OTHER ENDPOINT
    @GET
    public Uni<String> dummyEndpoint() {
        return Uni.createFrom().item("financial-goal");
    }
}
