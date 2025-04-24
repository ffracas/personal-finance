package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record BondNotFound(String message, String classHappen, LocalDate date) implements BondError {
    public BondNotFound(String classHappen) {
        this("Bond not found", classHappen, LocalDate.now());
    }
}
