package com.example.reactive.quarkus.personal.finance.converter;

import com.example.reactive.quarkus.personal.finance.model.entity.DepositAccount;
import com.example.reactive.quarkus.personal.finance.model.request.DepositAccountRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.DepositAccountResponseDto;

public class DepositAccountConverter implements Converter<DepositAccountRequestDto, DepositAccountResponseDto, DepositAccount> {
    @Override
    public DepositAccountResponseDto toDto(DepositAccount entity) {
        return new DepositAccountResponseDto(entity.getUser().getId().toString(), entity.getInvestedAmount(),
                entity.getAnnualRate(), entity.getStartDate(), entity.getEndDate());
    }

    @Override
    public DepositAccount toEntity(DepositAccountRequestDto dto) {
        DepositAccount deposit = new DepositAccount();
        // todo: deposit.user da vedere
        deposit.setAnnualRate(dto.annualRate());
        deposit.setInvestedAmount(dto.investedAmount());
        deposit.setStartDate(dto.startDate());
        deposit.setEndDate(dto.endDate());
        return deposit;
    }
}
