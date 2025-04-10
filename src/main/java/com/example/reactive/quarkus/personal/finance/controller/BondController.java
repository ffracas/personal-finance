package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.model.request.BondRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.BondResponseDto;
import com.example.reactive.quarkus.personal.finance.service.BondService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/bond")
public final class BondController {
    private final BondService bondService;

    public BondController(BondService bondService) {
        this.bondService = bondService;
    }


    @GET
    @Path("/getBondById/{bondId}")
    public Uni<Response> getBondById(@PathParam("bondId") String bondId) {
        return bondService.getBondById(bondId)
                .map(user -> Response.ok(user).status(Response.Status.OK).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/getAllBond")
    public Multi<Set<BondResponseDto>> getAllBond() {
        return bondService.getAllBonds();
    }

    @POST
    @Path("/createBond")
    public Uni<Response> createBond(BondRequestDto bondRequestDto) {
        return bondService.createBond(bondRequestDto)
                .map(user -> Response.ok(user).status(Response.Status.CREATED).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.BAD_REQUEST).build());
    }


    @PUT
    @Path("/updateBond/{bondId}")
    public Uni<Response> updateBond(BondRequestDto bondRequestDto, @PathParam("bondId") String bondId) {
        return bondService.updateBond(bondId, bondRequestDto)
                .map(user -> Response.ok(user).status(Response.Status.OK).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/deleteBond/{bondId}")
    public Uni<Response> deleteBond(@PathParam("bondId") String bondId) {
        return bondService.deleteBond(bondId)
                .map(response -> response ? Response.status(Response.Status.NO_CONTENT).build() : Response.status(Response.Status.NOT_FOUND).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }
}
