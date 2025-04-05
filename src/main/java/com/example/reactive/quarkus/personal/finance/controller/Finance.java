package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.model.response.FinanceResponseDto;
import com.example.reactive.quarkus.personal.finance.service.FinanceService;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/finance")
public class Finance {
    private final FinanceService financeService;

    public Finance(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Multi<FinanceResponseDto> getAllFinance() {
        System.out.println("getAllFinance");
        return financeService.getFinanceResponseDtos();
    }

}
