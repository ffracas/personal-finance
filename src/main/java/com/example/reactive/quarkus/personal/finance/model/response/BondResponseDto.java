package com.example.reactive.quarkus.personal.finance.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;


public record BondResponseDto(String bondId, String userId, String name, String code, BigDecimal investedAmount,
                              BigDecimal annualRate, LocalDate maturityDate, String couponType,
                              BigDecimal currentValue) {
}