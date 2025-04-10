package com.example.reactive.quarkus.personal.finance.converter;

import com.example.reactive.quarkus.personal.finance.model.entity.Bond;
import com.example.reactive.quarkus.personal.finance.model.entity.User;
import com.example.reactive.quarkus.personal.finance.model.request.BondRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.BondResponseDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public final class BondConverter implements Converter<BondRequestDto, BondResponseDto, Bond> {
    @Override
    public BondResponseDto toDto(Bond entity) {
        return new BondResponseDto(entity.getId().toString(), entity.getUser().getId().toString(), entity.getName(),
                entity.getCode(), entity.getInvestedAmount(), entity.getAnnualRate(), entity.getMaturityDate(),
                entity.getCouponType(), entity.getCurrentValue());
    }

    @Override
    public Bond toEntity(BondRequestDto dto) {
        Bond bond = new Bond();
        bond.setCode(dto.code());
        bond.setName(dto.name());
        bond.setInvestedAmount(dto.investedAmount());
        bond.setAnnualRate(dto.annualRate());
        bond.setMaturityDate(dto.maturityDate());
        bond.setCouponType(dto.couponType());
        bond.setCurrentValue(dto.currentValue());
        User user = new User();
        user.setId(UUID.fromString(dto.userId()));
        bond.setUser(user);
        return bond;
    }

    @Override
    public Bond toEntity(Long id, BondRequestDto dto) {
        return toEntity(dto);
    }
}
