package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record InvestmentNotFound(String message, String classHappen, LocalDate date) implements InvestmentError {
    public InvestmentNotFound(String classHappen) {
        this("Investment not found", classHappen, LocalDate.now());
    }
}
