package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record UserServerError(String message, String name, LocalDate time) implements UserError {
    public UserServerError(String message, String name) {
        this(message, name, LocalDate.now());
    }
}
