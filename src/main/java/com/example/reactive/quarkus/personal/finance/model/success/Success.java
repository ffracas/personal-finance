package com.example.reactive.quarkus.personal.finance.model.success;

public sealed interface Success permits InvestmentSuccess, RecurringExpenseSuccess, TransactionSuccess, UserSuccess {
}
