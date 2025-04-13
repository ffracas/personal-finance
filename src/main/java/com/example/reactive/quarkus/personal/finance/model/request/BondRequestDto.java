package com.example.reactive.quarkus.personal.finance.model.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BondRequestDto(String userId, String name, String code, BigDecimal investedAmount, BigDecimal annualRate,
                             LocalDate maturityDate, String couponType, BigDecimal currentValue) {
}