package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDate;

public record RecurringExpenseNotFound(String message, String classHappen,
                                       LocalDate date) implements RecurringExpenseError {
    public RecurringExpenseNotFound(String classHappen) {
        this("Recurring expense not found", classHappen, LocalDate.now());
    }
}
