package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.model.request.TransactionRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.TransactionResponseDto;
import com.example.reactive.quarkus.personal.finance.service.TransactionService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;

@Path("/transaction")
@Tag(name = "Transaction Operations", description = "Endpoints for managing transactions")
public final class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Retrieve a transaction by its ID", description = "Fetches the details of a specific transaction using its unique identifier.")
    @APIResponse(responseCode = "200", description = "Transaction retrieved successfully", content = @Content(schema = @Schema(implementation = TransactionResponseDto.class)))
    @APIResponse(responseCode = "404", description = "Transaction not found")
    @GET
    @Path("/getTransactionById/{transactionId}")
    public Uni<Response> getTransactionById(@PathParam("transactionId") String transactionId) {
        return transactionService.getTransactionById(transactionId)
                .map(transactionResponseDto -> Response.ok(transactionResponseDto).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND).entity(throwable).build());
    }


    @Operation(summary = "Retrieve all transactions", description = "Fetches a list of all transactions.")
    @APIResponse(responseCode = "200", description = "List of transactions retrieved successfully", content = @Content(schema = @Schema(implementation = TransactionResponseDto.class)))
    @GET
    @Path("/getAllTransaction")
    public Multi<Set<TransactionResponseDto>> getAllTransaction() {
        return transactionService.getAllTransaction();
    }

    @Operation(summary = "Create a new transaction", description = "Submits a new transaction to be processed.")
    @APIResponse(responseCode = "201", description = "Transaction created successfully", content = @Content(schema = @Schema(implementation = TransactionResponseDto.class)))
    @POST
    @Path("/createTransaction")
    public Uni<Response> createTransaction(TransactionRequestDto transactionRequestDto) {
        return transactionService.createTransaction(transactionRequestDto)
                .map(transactionResponseDto -> Response.status(Response.Status.CREATED).entity(transactionResponseDto).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable).build());
    }

    @Operation(summary = "Update an existing transaction", description = "Modifies details of an existing transaction.")
    @APIResponse(responseCode = "200", description = "Transaction updated successfully", content = @Content(schema = @Schema(implementation = TransactionResponseDto.class)))
    @APIResponse(responseCode = "404", description = "Transaction not found")
    @PUT
    @Path("/updateTransaction/{transactionId}")
    public Uni<Response> updateTransaction(TransactionRequestDto transactionRequestDto, @PathParam("transactionId") String transactionId) {
        return transactionService.updateTransaction(transactionRequestDto, transactionId)
                .map(user -> Response.ok(user).status(Response.Status.OK).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @Operation(summary = "Delete a transaction", description = "Removes a transaction from the system using its ID.")
    @APIResponse(responseCode = "204", description = "Transaction deleted successfully")
    @APIResponse(responseCode = "404", description = "Transaction not found")
    @DELETE
    @Path("/deleteTransaction/{transactionId}")
    public Uni<Response> deleteTransaction(@PathParam("transactionId") String transactionId) {
        return transactionService.deleteTransaction(transactionId)
                .map(response -> response ? Response.status(Response.Status.NO_CONTENT).build() : Response.status(Response.Status.NOT_FOUND).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }
}
