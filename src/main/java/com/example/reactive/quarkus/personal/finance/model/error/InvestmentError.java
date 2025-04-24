package com.example.reactive.quarkus.personal.finance.model.error;

public sealed interface InvestmentError extends Error permits InvestmentNotFound, InvestmentServerError {
}
