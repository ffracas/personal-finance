package com.example.reactive.quarkus.personal.finance.model.error;

public sealed interface UserError extends Error permits UserNotFound, UserServerError {
}
