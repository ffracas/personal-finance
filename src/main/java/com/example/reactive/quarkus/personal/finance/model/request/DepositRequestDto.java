package com.example.reactive.quarkus.personal.finance.model.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DepositRequestDto (String userId, BigDecimal investedAmount, BigDecimal annualRate,
                                 LocalDate startDate, LocalDate endDate) {
}