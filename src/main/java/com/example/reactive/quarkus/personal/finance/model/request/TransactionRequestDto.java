package com.example.reactive.quarkus.personal.finance.model.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDto(String userId, BigDecimal amount, String category, String subCategory,
                                    String type, LocalDate transactionDate) {
}