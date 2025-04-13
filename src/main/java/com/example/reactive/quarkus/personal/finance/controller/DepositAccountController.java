package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.model.request.DepositAccountRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.DepositAccountResponseDto;
import com.example.reactive.quarkus.personal.finance.service.DepositService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;

@Path("/deposit-account")
@Tag(name = "Deposit", description = "Deposit API")
public class DepositAccountController {

    private final DepositService depositService;


    public DepositAccountController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GET
    @Path("/getAllDepositAccount")
    @Operation(summary = "Retrieve all Deposit Account", description = "Returns a list of all investments.")
    @APIResponse(responseCode = "200", description = "All deposit account retrieved successfully")
    public Multi<Set<DepositAccountResponseDto>> getAllDepositAccount() {
        return depositService.getAllDepositAccount();
    }

    @GET
    @Path("/getDepositAccountById/{depositAccountId}")
    @Operation(summary = "Get a deposit account by ID", description = "Retrieves a deposit account by its unique identifier.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Deposit account found and returned"),
            @APIResponse(responseCode = "404", description = "Deposit account not found")
    })
    public Uni<Response> getDepositAccountById(@PathParam("depositAccountId") String investmentId) {
        return depositService.getDepositAccountById(investmentId)
                .map(investmentResponseDto -> Response.ok().entity(investmentResponseDto).build())
                .onFailure()
                .recoverWithItem(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/createDepositAccount")
    @Operation(summary = "Create new deposit account", description = "Create a new deposit account for a user")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Deposit account created successfully"),
            @APIResponse(responseCode = "500", description = "Internal server error during deposit account creation")
    })
    public Uni<Response> createNewDepositAccount(@RequestBody DepositAccountRequestDto newDeposit) {
        return depositService.createDepositAccount(newDeposit)
                .map(newDepositRespDto -> Response.status(Response.Status.CREATED).entity(newDepositRespDto).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }

    @PUT
    @Path("/updateDepositAccount/{depositId}")
    @Operation(summary = "Update an existing deposit account", description = "Update an existing account for a user")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Deposit account update successfully"),
            @APIResponse(responseCode = "500", description = "Internal server error during deposit account creation")
    })
    public Uni<Response> updateDepositAccount(@PathParam("depositId") String depositId,
                                              @RequestBody DepositAccountRequestDto updatedDeposit) {
        return depositService.updateDepositAccount(depositId, updatedDeposit)
                .map(depositResponseDto -> Response.ok().entity(depositResponseDto).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }

}
