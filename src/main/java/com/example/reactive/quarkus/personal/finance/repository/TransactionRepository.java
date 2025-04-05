package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.model.entity.Transaction;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

/**
 * Repository class for managing transactions in the data store.
 * This class extends {@link PanacheRepositoryBase} to leverage Quarkus' Panache framework,
 * providing built-in methods for CRUD operations on the {@link Transaction} entity.
 * The repository provides methods for retrieving, saving, and deleting transactions.
 * Each method is transactional, ensuring the integrity of database operations.
 *
 * <p>The methods in this repository utilize reactive programming principles,
 * returning types from the Mutiny library, specifically {@link Uni}, to allow asynchronous
 * and non-blocking database interactions.</p>
 *
 * <p>Each method in this repository is annotated with {@link WithTransaction}, ensuring that
 * the operations are executed within a transaction context.</p>
 */
@ApplicationScoped
public class TransactionRepository implements PanacheRepositoryBase<Transaction, UUID> {

    /**
     * Retrieves a transaction by its unique identifier.
     * This method queries the database for a transaction with the given {@link UUID} and returns
     * a {@link Uni} containing the transaction if it is found or empty if no transaction matches the ID.
     *
     * <p>The method is annotated with {@link WithTransaction}, ensuring that the operation is
     * executed within a transaction context.</p>
     *
     * @param id the unique identifier of the transaction to retrieve.
     * @return a {@link Uni} containing the {@link Transaction} entity or empty if no transaction is found.
     */
    @WithTransaction
    public Uni<Transaction> getTransactionById(UUID id) {
        return findById(id);
    }

    /**
     * Retrieves all transactions from the data store.
     * This method returns a list of all transactions as a {@link Uni} containing a {@link List} of
     * {@link Transaction} entities.
     *
     * <p>The method is annotated with {@link WithTransaction}, ensuring that the operation is
     * executed within a transaction context.</p>
     *
     * @return a {@link Uni} containing a list of all {@link Transaction} entities in the data store.
     */
    @WithTransaction
    public Uni<List<Transaction>> getAllTransactions() {
        return listAll();
    }

    /**
     * Saves a new or updated transaction to the data store.
     * This method persists the provided {@link Transaction} entity. If the transaction does not exist,
     * it will be created. If it exists, it will be updated. The method returns a {@link Uni} containing the
     * persisted {@link Transaction} entity.
     *
     * <p>If the persistence operation fails, the error is logged using {@link Log#error}.</p>
     *
     * <p>The method is annotated with {@link WithTransaction}, ensuring that the operation is
     * executed within a transaction context.</p>
     *
     * @param transaction the transaction entity to persist.
     * @return a {@link Uni} containing the persisted {@link Transaction} entity.
     */
    @WithTransaction
    public Uni<Transaction> saveTransaction(Transaction transaction) {
        return persist(transaction)
                .onFailure()
                .invoke(Log::error);
    }

    /**
     * Deletes a transaction from the data store by its unique identifier.
     * This method removes the transaction with the specified {@link UUID} from the data store and returns
     * a {@link Uni} containing a {@code Boolean} indicating whether the deletion was successful.
     *
     * <p>The method is annotated with {@link WithTransaction}, ensuring that the operation is
     * executed within a transaction context.</p>
     *
     * @param transaction the unique identifier of the transaction to delete.
     * @return a {@link Uni} containing {@code true} if the transaction was successfully deleted,
     * or {@code false} if no transaction with the given ID was found.
     */
    @WithTransaction
    public Uni<Boolean> deleteTransaction(UUID transaction) {
        return deleteById(transaction);
    }
}

