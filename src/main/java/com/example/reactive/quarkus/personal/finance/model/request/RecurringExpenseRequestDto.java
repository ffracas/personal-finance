package com.example.reactive.quarkus.personal.finance.model.request;

import java.math.BigDecimal;
import java.time.LocalDate;


public record RecurringExpenseRequestDto(String userId, BigDecimal amount, String category, String frequency,
                                         LocalDate startDate, LocalDate endDate) {
}