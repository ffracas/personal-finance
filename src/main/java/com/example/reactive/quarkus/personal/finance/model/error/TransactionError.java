package com.example.reactive.quarkus.personal.finance.model.error;

public sealed interface TransactionError extends Error permits TransactionNotFound, TransactionServerError {
}
