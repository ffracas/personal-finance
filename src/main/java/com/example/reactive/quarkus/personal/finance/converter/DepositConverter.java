package com.example.reactive.quarkus.personal.finance.converter;

import com.example.reactive.quarkus.personal.finance.model.entity.DepositAccount;
import com.example.reactive.quarkus.personal.finance.model.entity.User;
import com.example.reactive.quarkus.personal.finance.model.request.DepositRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.DepositResponseDto;

import java.util.UUID;

public class DepositConverter implements Converter<DepositRequestDto, DepositResponseDto, DepositAccount> {
    @Override
    public DepositResponseDto toDto(DepositAccount entity) {
        return new DepositResponseDto(entity.getUser().getId(), entity.getInvestedAmount(), entity.getAnnualRate(),
                entity.getStartDate(), entity.getEndDate());
    }

    @Override
    public DepositAccount toEntity(DepositRequestDto dto) {
        DepositAccount deposit = new DepositAccount();
        User user = new User();
        user.setId(UUID.fromString(dto.userId()));
        deposit.setUser(user);
        deposit.setAnnualRate(dto.annualRate());
        deposit.setInvestedAmount(dto.investedAmount());
        deposit.setStartDate(dto.startDate());
        deposit.setEndDate(dto.endDate());
        return deposit;
    }

    @Override
    public DepositAccount toEntity(Long id, DepositRequestDto dto) {
        DepositAccount deposit = toEntity(dto);
        deposit.id = id;
        return deposit;
    }
}
