package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.model.request.RecurringExpenseRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.RecurringExpenseResponseDto;
import com.example.reactive.quarkus.personal.finance.service.RecurringExpenseService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;

@Path("/recurring-expense")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Recurring Expense Management", description = "Operations related to recurring expenses")
public final class RecurringExpenseController {

    private final RecurringExpenseService recurringExpenseService;

    public RecurringExpenseController(RecurringExpenseService recurringExpenseService) {
        this.recurringExpenseService = recurringExpenseService;
    }

    @GET
    @Path("/getAllRecurringExpense")
    @Operation(summary = "Retrieve all recurring expenses")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Successfully retrieved list"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Multi<Set<RecurringExpenseResponseDto>> getAllRecurringExpense() {
        return recurringExpenseService.getAllRecurringExpense();
    }

    @GET
    @Path("/getRecurringExpenseById/{recurringExpenseId}")
    @Operation(summary = "Find recurring expense by ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Successfully found the recurring expense"),
            @APIResponse(responseCode = "404", description = "Recurring expense not found"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Uni<RecurringExpenseResponseDto> getRecurringExpenseById(@PathParam("recurringExpenseId") Long recurringExpenseId) {
        return recurringExpenseService.getRecurringExpenseById(recurringExpenseId);
    }

    @POST
    @Path("/createRecurringExpense")
    @Operation(summary = "Create a new recurring expense")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Recurring expense created successfully"),
            @APIResponse(responseCode = "400", description = "Invalid input"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Uni<RecurringExpenseResponseDto> createRecurringExpense(RecurringExpenseRequestDto recurringExpenseRequestDto) {
        return recurringExpenseService.createRecurringExpense(recurringExpenseRequestDto);
    }

    @PUT
    @Path("/updateRecurringExpense")
    @Operation(summary = "Update an existing recurring expense")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Recurring expense updated successfully"),
            @APIResponse(responseCode = "400", description = "Invalid input"),
            @APIResponse(responseCode = "404", description = "Recurring expense not found"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Uni<RecurringExpenseResponseDto> updateRecurringExpense(RecurringExpenseRequestDto recurringExpenseRequestDto) {
        return recurringExpenseService.updateRecurringExpense(recurringExpenseRequestDto);
    }

    @DELETE
    @Path("/deleteRecurringExpense")
    @Operation(summary = "Delete an existing recurring expense")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Recurring expense deleted successfully"),
            @APIResponse(responseCode = "400", description = "Invalid input"),
            @APIResponse(responseCode = "404", description = "Recurring expense not found"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Uni<Void> deleteRecurringExpense(RecurringExpenseRequestDto recurringExpenseRequestDto) {
        return recurringExpenseService.deleteRecurringExpense(recurringExpenseRequestDto);
    }
}
