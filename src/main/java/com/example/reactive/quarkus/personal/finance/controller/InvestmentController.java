package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.model.request.InvestmentRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.InvestmentResponseDto;
import com.example.reactive.quarkus.personal.finance.service.InvestmentService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;

/**
 * REST controller for managing investments.
 * <p>
 * This controller exposes endpoints for creating, reading, updating, and deleting investments.
 * It uses MicroProfile OpenAPI annotations to generate Swagger documentation.
 * </p>
 */
@Path("/investment")
@Tag(name = "Investment", description = "Investment CRUD operations")
public final class InvestmentController {

    private final InvestmentService investmentService;

    /**
     * Constructs an InvestmentController with the provided InvestmentService.
     *
     * @param investmentService the service used for investment business operations
     */
    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    /**
     * Retrieves all investments.
     *
     * @return a reactive stream (Multi) containing sets of InvestmentResponseDto representing all investments
     */
    @GET
    @Path("/getAllInvestment")
    @Operation(summary = "Retrieve all investments", description = "Returns a list of all investments.")
    @APIResponse(responseCode = "200", description = "All investments retrieved successfully")
    public Multi<Set<InvestmentResponseDto>> getAllInvestment() {
        return investmentService.getAllInvestments();
    }

    /**
     * Retrieves a specific investment by its unique identifier.
     *
     * @param investmentId the unique identifier (UUID as a string) of the investment
     * @return a Uni emitting a Response containing the InvestmentResponseDto if found,
     * or a NOT_FOUND status if the investment does not exist
     */
    @GET
    @Path("/getInvestmentById/{investmentId}")
    @Operation(summary = "Get an investment by ID", description = "Retrieves an investment by its unique identifier.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Investment found and returned"),
            @APIResponse(responseCode = "404", description = "Investment not found")
    })
    public Uni<Response> getInvestmentById(@PathParam("investmentId") String investmentId) {
        return investmentService.getInvestmentById(investmentId)
                .map(investmentResponseDto -> Response.ok().entity(investmentResponseDto).build())
                .onFailure()
                .recoverWithItem(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Creates a new investment.
     *
     * @param investmentRequestDto the DTO containing investment details to be created
     * @return a Uni emitting a Response with the created InvestmentResponseDto and a CREATED status,
     * or an INTERNAL_SERVER_ERROR status in case of failure
     */
    @POST
    @Path("/createInvestment")
    @Operation(summary = "Create an investment", description = "Creates a new investment record.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Investment created successfully"),
            @APIResponse(responseCode = "500", description = "Internal server error during investment creation")
    })
    public Uni<Response> createInvestment(InvestmentRequestDto investmentRequestDto) {
        return investmentService.createInvestment(investmentRequestDto)
                .map(investmentResponseDto -> Response.status(Response.Status.CREATED).entity(investmentResponseDto).build())
                .onFailure()
                .recoverWithItem(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Updates an existing investment.
     *
     * @param investmentId         the unique identifier (UUID as a string) of the investment to update
     * @param investmentRequestDto the DTO containing updated investment details
     * @return a Uni emitting a Response with the updated InvestmentResponseDto and an OK status,
     * or a NOT_FOUND status if the investment does not exist
     */
    @PUT
    @Path("/updateInvestment/{investmentId}")
    @Operation(summary = "Update an investment", description = "Updates an existing investment by its unique identifier.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Investment updated successfully"),
            @APIResponse(responseCode = "404", description = "Investment not found")
    })
    public Uni<Response> updateInvestment(@PathParam("investmentId") String investmentId, InvestmentRequestDto investmentRequestDto) {
        return investmentService.updateInvestment(investmentRequestDto, investmentId)
                .map(user -> Response.ok(user).status(Response.Status.OK).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Deletes an investment by its unique identifier.
     *
     * @param investmentId the unique identifier (UUID as a string) of the investment to delete
     * @return a Uni emitting a Response with a NO_CONTENT status if deletion was successful,
     * or a NOT_FOUND or INTERNAL_SERVER_ERROR status otherwise
     */
    @DELETE
    @Path("/deleteInvestment/{investmentId}")
    @Operation(summary = "Delete an investment", description = "Deletes an investment by its unique identifier.")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Investment deleted successfully"),
            @APIResponse(responseCode = "404", description = "Investment not found"),
            @APIResponse(responseCode = "500", description = "Internal server error during deletion")
    })
    public Uni<Response> deleteInvestment(@PathParam("investmentId") String investmentId) {
        return investmentService.deleteInvestmentById(investmentId)
                .map(result -> result
                        ? Response.status(Response.Status.NO_CONTENT).build()
                        : Response.status(Response.Status.NOT_FOUND).build())
                .onFailure()
                .recoverWithItem(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }
}
