package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record TransactionNotFound(String message, LocalDate date, String classHappen) implements TransactionError {
    public TransactionNotFound(String classHappen) {
        this("Transaction not found", LocalDate.now(), classHappen);
    }
}
