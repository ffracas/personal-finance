package com.example.reactive.quarkus.personal.finance.converter;

import com.example.reactive.quarkus.personal.finance.model.entity.Investment;
import com.example.reactive.quarkus.personal.finance.model.entity.User;
import com.example.reactive.quarkus.personal.finance.model.request.InvestmentRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.InvestmentResponseDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public final class InvestmentConverter implements Converter<InvestmentRequestDto, InvestmentResponseDto, Investment> {
    @Override
    public InvestmentResponseDto toDto(Investment entity) {
        return new InvestmentResponseDto(entity.getId().toString(), entity.getUser().getId().toString(), entity.getName(),
                entity.getCode(), entity.getSharesOwned(), entity.getUnitPrice(), entity.getInvestmentDate(), entity.getCurrentValue());
    }

    @Override
    public Investment toEntity(InvestmentRequestDto dto) {
        Investment investment = new Investment();
        investment.setCode(dto.code());
        investment.setName(dto.name());
        investment.setCurrentValue(dto.currentValue());
        investment.setInvestmentDate(dto.investmentDate());
        investment.setUnitPrice(dto.unitPrice());
        investment.setSharesOwned(dto.sharedOwned());
        User user = new User();
        user.setId(UUID.fromString(dto.userId()));
        investment.setUser(user);
        return investment;
    }

    @Override
    public Investment toEntity(Long id, InvestmentRequestDto dto) {
        return toEntity(dto);
    }
}
