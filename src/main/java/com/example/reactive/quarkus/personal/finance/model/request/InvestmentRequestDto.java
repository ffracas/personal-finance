package com.example.reactive.quarkus.personal.finance.model.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentRequestDto(String userId, String name, String code, BigDecimal sharedOwned,
                                   BigDecimal unitPrice, LocalDate investmentDate, BigDecimal currentValue) {
}