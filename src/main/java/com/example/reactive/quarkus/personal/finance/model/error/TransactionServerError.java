package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record TransactionServerError(String message, LocalDate date, String classHappen) implements TransactionError {
    public TransactionServerError(String message, String name) {
        this(message, LocalDate.now(), name);
    }
}
