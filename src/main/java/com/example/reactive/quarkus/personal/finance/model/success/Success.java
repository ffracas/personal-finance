package com.example.reactive.quarkus.personal.finance.model.success;

public sealed interface Success permits BondSuccess, InvestmentSuccess, RecurringExpenseSuccess, TransactionSuccess, UserSuccess {
}
