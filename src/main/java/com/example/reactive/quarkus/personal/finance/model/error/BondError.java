package com.example.reactive.quarkus.personal.finance.model.error;

public sealed interface BondError extends Error permits BondNotFound, BondServerError {
}
