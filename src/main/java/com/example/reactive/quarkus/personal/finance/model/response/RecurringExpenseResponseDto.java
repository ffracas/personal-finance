package com.example.reactive.quarkus.personal.finance.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurringExpenseResponseDto(String recurringExpenseId, String userId, BigDecimal amount, String category,
                                          String frequency, LocalDate startDate, LocalDate endDate) {
}