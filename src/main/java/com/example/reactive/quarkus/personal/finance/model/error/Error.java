package com.example.reactive.quarkus.personal.finance.model.error;

public sealed interface Error permits BondError, GenericError, InvestmentError, RecurringExpenseError, TransactionError, UserError {
}
