package com.example.reactive.quarkus.personal.finance.model.error;

public sealed interface RecurringExpenseError extends Error permits RecurringExpenseNotFound, RecurringExpenseServerError {
}
