package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.model.entity.RecurringExpense;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;

/**
 * Repository class for managing {@link RecurringExpense} entities using reactive patterns.
 * <p>
 * This class is annotated with {@code @ApplicationScoped}, meaning a single instance is created and
 * shared throughout the application. It implements {@link PanacheRepositoryBase}, which provides
 * out-of-the-box CRUD operations and query capabilities using Hibernate Reactive with Panache.
 * <p>
 * All methods are annotated with {@code @WithTransaction} to ensure that each database operation
 * executes within its own transactional context.
 * <p>
 * The repository leverages {@link Uni} from Mutiny to support non-blocking, asynchronous interaction
 * with the underlying database.
 * <p>
 * Example usage:
 * <pre>{@code
 *     recurringExpenseRepository.getAllRecurringExpenses()
 *         .subscribe().with(expenses -> {
 *             // handle result
 *         });
 * }</pre>
 */
@ApplicationScoped
public final class RecurringExpenseRepository implements PanacheRepositoryBase<RecurringExpense, UUID> {

    private static final Logger Log = Logger.getLogger(RecurringExpenseRepository.class);

    /**
     * Retrieves a single {@link RecurringExpense} by its unique identifier.
     *
     * @param recurringExpenseId the UUID of the recurring expense to retrieve
     * @return a {@link Uni} emitting the found {@link RecurringExpense}, or {@code null} if not found
     */
    @WithTransaction
    public Uni<RecurringExpense> getRecurringExpenseById(UUID recurringExpenseId) {
        return findById(recurringExpenseId);
    }

    /**
     * Retrieves all {@link RecurringExpense} entities from the database.
     *
     * @return a {@link Uni} emitting a {@link List} of all {@link RecurringExpense} entries
     */
    @WithTransaction
    public Uni<List<RecurringExpense>> getAllRecurringExpenses() {
        return listAll();
    }

    /**
     * Persists a new {@link RecurringExpense} entity or updates an existing one in the database.
     * <p>
     * If an error occurs during persistence, the error is logged using {@link Logger#error(Object)}.
     *
     * @param recurringExpense the {@link RecurringExpense} instance to save
     * @return a {@link Uni} emitting the persisted {@link RecurringExpense}
     */
    @WithTransaction
    public Uni<RecurringExpense> saveRecurringExpense(RecurringExpense recurringExpense) {
        return persist(recurringExpense)
                .onFailure()
                .invoke(Log::error);
    }

    /**
     * Deletes a {@link RecurringExpense} entity by its unique identifier.
     *
     * @param recurringExpenseId the UUID of the recurring expense to delete
     * @return a {@link Uni} emitting {@code true} if the entity was deleted successfully, {@code false} otherwise
     */
    @WithTransaction
    public Uni<Boolean> deleteRecurringExpense(UUID recurringExpenseId) {
        return deleteById(recurringExpenseId);
    }
}
