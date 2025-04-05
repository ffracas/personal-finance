package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.model.entity.RecurringExpense;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public final class RecurringExpenseRepository implements PanacheRepositoryBase<RecurringExpense, UUID> {
    @WithTransaction
    public Uni<RecurringExpense> getRecurringExpenseById(UUID recurringExpenseId) {
        return findById(recurringExpenseId);
    }

    @WithTransaction
    public Uni<List<RecurringExpense>> getAllRecurringExpenses() {
        return listAll();
    }

    @WithTransaction
    public Uni<RecurringExpense> saveRecurringExpense(RecurringExpense recurringExpense) {
        return persist(recurringExpense)
                .onFailure()
                .invoke(Log::error);
    }

    @WithTransaction
    public Uni<Boolean> deleteRecurringExpense(UUID recurringExpenseId) {
        return deleteById(recurringExpenseId);
    }
}
