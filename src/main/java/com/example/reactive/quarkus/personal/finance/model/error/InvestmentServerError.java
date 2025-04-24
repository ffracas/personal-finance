package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record InvestmentServerError(String message, String classHappen, LocalDate date) implements InvestmentError {
    public InvestmentServerError(String message, String classHappen) {
        this(message, classHappen, LocalDate.now());
    }
}
