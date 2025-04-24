package com.example.reactive.quarkus.personal.finance.model.success;

public sealed interface TransactionSuccess extends Success permits TransactionDeleteOk {
}
