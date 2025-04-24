package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record BondServerError(String message, String classHappen, LocalDate date) implements BondError {
    public BondServerError(String message, String classHappen) {
        this(message, classHappen, LocalDate.now());
    }
}
