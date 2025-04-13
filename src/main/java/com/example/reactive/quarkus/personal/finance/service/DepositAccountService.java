package com.example.reactive.quarkus.personal.finance.service;

import com.example.reactive.quarkus.personal.finance.converter.DepositAccountConverter;
import com.example.reactive.quarkus.personal.finance.model.request.DepositAccountRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.DepositAccountResponseDto;
import com.example.reactive.quarkus.personal.finance.repository.DepositAccountRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class DepositAccountService {

    private final DepositAccountRepository depositRepo;
    private final DepositAccountConverter converter;

    public DepositAccountService(DepositAccountRepository depositRepo, DepositAccountConverter converter) {
        this.depositRepo = depositRepo;
        this.converter = converter;
    }

    public Multi<Set<DepositAccountResponseDto>> getAllDepositAccount() {
        return depositRepo.getAllDepositAccount()
                .toMulti()
                .map(converter::toDto);
    }

    public Uni<DepositAccountResponseDto> getDepositAccountById(String depositId) {
        return depositRepo.getDepositAccountById(UUID.fromString(depositId))
                .map(converter::toDto);
    }

    public Uni<DepositAccountResponseDto> createDepositAccount(DepositAccountRequestDto newDepositAccount) {
        return depositRepo.saveDepositAccount(converter.toEntity(newDepositAccount))
                .map(converter::toDto);
    }

    public Uni<DepositAccountResponseDto> updateDepositAccount(String depositId, DepositAccountRequestDto requestDto) {
        return depositRepo.getDepositAccountById(UUID.fromString(depositId))
                .onItem().transformToUni(depositAccount -> {
                    if (depositAccount.getUser().getId().equals(UUID.fromString(requestDto.userId())))
                        return depositRepo.saveDepositAccount(depositAccount).map(converter::toDto);
                    else
                        return Uni.createFrom()
                                .failure(new WebApplicationException("User ID mismatch", Response.Status.CONFLICT));
                });
    }

    public Uni<Boolean> deleteDepositAccount(String depositId) {
        return depositRepo.deleteDepositAccountById(UUID.fromString(depositId));
    }
}
