package com.example.reactive.quarkus.personal.finance.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurringExpenseResponseDto(BigDecimal amount, String category, String frequency, LocalDate startDate,
                                          LocalDate endDate) {
}