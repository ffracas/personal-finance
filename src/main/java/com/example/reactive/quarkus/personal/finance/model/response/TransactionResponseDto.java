package com.example.reactive.quarkus.personal.finance.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDto(UUID userId, String transactionId, BigDecimal amount, String category,
                                     String subCategory,
                                     String type, LocalDate transactionDate) {
}