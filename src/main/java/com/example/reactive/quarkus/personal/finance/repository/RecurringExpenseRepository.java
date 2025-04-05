package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.model.entity.RecurringExpense;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public final class RecurringExpenseRepository implements PanacheRepository<RecurringExpense> {
    @WithTransaction
    public Uni<RecurringExpense> getRecurringExpenseById(long id) {
        return findById(id);
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
    public Uni<Void> deleteRecurringExpense(RecurringExpense recurringExpense) {
        return delete(recurringExpense)
                .onFailure()
                .invoke(Log::error);
    }
}
