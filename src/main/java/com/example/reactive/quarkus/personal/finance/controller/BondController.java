package com.example.reactive.quarkus.personal.finance.controller;

import com.example.reactive.quarkus.personal.finance.model.request.BondRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.BondResponseDto;
import com.example.reactive.quarkus.personal.finance.service.BondService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.Set;

import static com.example.reactive.quarkus.personal.finance.utility.ProcessResponse.processEmptyResponse;
import static com.example.reactive.quarkus.personal.finance.utility.ProcessResponse.processTheResultFromService;

@Path("/bond")
public final class BondController {
    private final BondService bondService;

    public BondController(BondService bondService) {
        this.bondService = bondService;
    }


    @GET
    @Path("/getBondById/{bondId}")
    public Uni<Response> getBondById(@PathParam("bondId") String bondId) {
        return processTheResultFromService(bondService.getBondById(bondId), Response.Status.OK);
    }

    @GET
    @Path("/getAllBond")
    public Multi<Set<BondResponseDto>> getAllBond() {
        return bondService.getAllBonds();
    }

    @POST
    @Path("/createBond")
    public Uni<Response> createBond(BondRequestDto bondRequestDto) {
        return processTheResultFromService(bondService.createBond(bondRequestDto), Response.Status.CREATED);
    }


    @PUT
    @Path("/updateBond/{bondId}")
    public Uni<Response> updateBond(BondRequestDto bondRequestDto, @PathParam("bondId") String bondId) {
        return processTheResultFromService(bondService.updateBond(bondId, bondRequestDto), Response.Status.OK);
    }

    @DELETE
    @Path("/deleteBond/{bondId}")
    public Uni<Response> deleteBond(@PathParam("bondId") String bondId) {
        return processEmptyResponse(bondService.deleteBond(bondId));
    }
}
