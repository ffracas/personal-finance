package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record RecurringExpenseServerError(String message, String classHappen,
                                          LocalDate date) implements RecurringExpenseError {
    public RecurringExpenseServerError(String message, String classHappen) {
        this(message, classHappen, LocalDate.now());

    }
}
