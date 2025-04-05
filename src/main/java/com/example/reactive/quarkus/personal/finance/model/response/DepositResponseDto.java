package com.example.reactive.quarkus.personal.finance.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DepositResponseDto (String userId, BigDecimal investedAmount, BigDecimal annualRate,
                                  LocalDate startDate, LocalDate endDate) {
}
