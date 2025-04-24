package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.functional.Either;
import com.example.reactive.quarkus.personal.finance.model.entity.RecurringExpense;
import com.example.reactive.quarkus.personal.finance.model.error.Error;
import com.example.reactive.quarkus.personal.finance.model.error.RecurringExpenseNotFound;
import com.example.reactive.quarkus.personal.finance.model.error.RecurringExpenseServerError;
import com.example.reactive.quarkus.personal.finance.model.success.RecurringExpenseDeleteOk;
import com.example.reactive.quarkus.personal.finance.model.success.Success;
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

    private static Uni<Either<Error, RecurringExpense>> processResponse(Uni<RecurringExpense> recurringExpenseUni) {
        return recurringExpenseUni
                .<Either<Error, RecurringExpense>>map(recurringExpense -> recurringExpense != null ? Either.right(recurringExpense) : Either.left(new RecurringExpenseNotFound(RecurringExpenseRepository.class.getName())))
                .onFailure()
                .recoverWithItem(throwable -> Either.left(new RecurringExpenseServerError(throwable.getMessage(), RecurringExpenseRepository.class.getName())));
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
     * Retrieves a single {@link RecurringExpense} by its unique identifier.
     *
     * @param recurringExpenseId the UUID of the recurring expense to retrieve
     * @return a {@link Uni} emitting the found {@link RecurringExpense}, or {@code null} if not found
     */
    @WithTransaction
    public Uni<Either<Error, RecurringExpense>> getRecurringExpenseById(UUID recurringExpenseId) {
        return processResponse(findById(recurringExpenseId));
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
    public Uni<Either<Error, RecurringExpense>> saveRecurringExpense(RecurringExpense recurringExpense) {
        return processResponse(persist(recurringExpense));
    }

    /**
     * Deletes a {@link RecurringExpense} entity by its unique identifier.
     *
     * @param recurringExpenseId the UUID of the recurring expense to delete
     * @return a {@link Uni} emitting {@code true} if the entity was deleted successfully, {@code false} otherwise
     */
    @WithTransaction
    public Uni<Either<Error, Success>> deleteRecurringExpense(UUID recurringExpenseId) {
        return deleteById(recurringExpenseId)
                .map(result -> Boolean.TRUE.equals(result) ? Either.right(new RecurringExpenseDeleteOk()) : Either.left(new RecurringExpenseNotFound(RecurringExpenseRepository.class.getName())));
    }
}
