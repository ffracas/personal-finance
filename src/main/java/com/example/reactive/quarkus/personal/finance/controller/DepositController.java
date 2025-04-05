package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.service.DepositService;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/deposit")
@Tag(name = "Deposit", description = "Deposit API")
public class DepositController {

    private final DepositService depositService;


    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }



}
