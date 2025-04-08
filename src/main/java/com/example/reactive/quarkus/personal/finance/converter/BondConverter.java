package com.example.reactive.quarkus.personal.finance.converter;

import com.example.reactive.quarkus.personal.finance.model.entity.Bond;
import com.example.reactive.quarkus.personal.finance.model.request.BondRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.BondResponseDto;

public class BondConverter implements Converter<BondRequestDto, BondResponseDto, Bond> {
    @Override
    public BondResponseDto toDto(Bond entity) {
        return null;
    }

    @Override
    public Bond toEntity(BondRequestDto dto) {
        return null;
    }

    @Override
    public Bond toEntity(Long id, BondRequestDto dto) {
        return null;
    }
}
