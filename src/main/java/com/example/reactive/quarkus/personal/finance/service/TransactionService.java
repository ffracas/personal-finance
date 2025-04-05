package com.example.reactive.quarkus.personal.finance.service;

import com.example.reactive.quarkus.personal.finance.converter.TransactionConverter;
import com.example.reactive.quarkus.personal.finance.model.entity.Transaction;
import com.example.reactive.quarkus.personal.finance.model.request.TransactionRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.TransactionResponseDto;
import com.example.reactive.quarkus.personal.finance.repository.TransactionRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;
import java.util.UUID;

/**
 * Service class responsible for managing transactions.
 * This class provides methods for CRUD operations on transactions,
 * such as retrieving, creating, updating, and deleting transactions.
 * It uses {@link TransactionRepository} for data access and {@link TransactionConverter}
 * for converting between entity objects and data transfer objects (DTOs).
 *
 * <p>The operations provided by this service layer ensure that transaction data is handled
 * correctly and consistently. The service is designed to be used in a Quarkus application,
 * leveraging the reactive programming paradigm with the return types {@link Uni} and {@link Multi}
 * from the Mutiny library.</p>
 *
 * <p>Each method is responsible for a specific operation:
 * - {@link #getTransactionById(String)} retrieves a transaction by its ID.
 * - {@link #getAllTransaction()} retrieves all transactions.
 * - {@link #createTransaction(TransactionRequestDto)} creates a new transaction.
 * - {@link #updateTransaction(TransactionRequestDto, String)} updates an existing transaction.
 * - {@link #deleteTransaction(String)} deletes a transaction by its ID.</p>
 */
@ApplicationScoped
public final class TransactionService {

    /**
     * The repository used for interacting with the transaction data.
     * It provides methods for saving, retrieving, and deleting transactions from the data store.
     */
    private final TransactionRepository transactionRepository;

    /**
     * The converter used to transform between entity objects and DTOs.
     * It is used for converting {@link Transaction} entities to {@link TransactionResponseDto}
     * and vice versa.
     */
    private final TransactionConverter transactionConverter;

    /**
     * Constructor for creating an instance of {@link TransactionService}.
     * This constructor is typically invoked by the dependency injection container in Quarkus.
     *
     * @param transactionRepository the repository responsible for transaction data management.
     * @param transactionConverter  the converter responsible for transforming transaction entities.
     */
    public TransactionService(TransactionRepository transactionRepository, TransactionConverter transactionConverter) {
        this.transactionRepository = transactionRepository;
        this.transactionConverter = transactionConverter;
    }

    /**
     * Helper method to update an existing {@link Transaction} entity with values from the provided DTO.
     * This method modifies the given transaction and updates its fields based on the data in the
     * provided {@link TransactionRequestDto}.
     *
     * @param transaction           the transaction entity to update.
     * @param transactionRequestDto the DTO containing the updated transaction data.
     * @return the updated {@link Transaction} entity.
     */
    private static Transaction updateTransaction(Transaction transaction, TransactionRequestDto transactionRequestDto) {
        transaction.setTransactionDate(transactionRequestDto.transactionDate());
        transaction.setAmount(transactionRequestDto.amount());
        transaction.setType(transactionRequestDto.type().toLowerCase());
        transaction.setCategory(transactionRequestDto.category());
        transaction.setSubCategory(transactionRequestDto.subCategory());
        return transaction;
    }

    /**
     * Retrieves a transaction by its unique ID.
     * This method queries the repository for a transaction matching the given ID
     * and returns a DTO representation of the transaction.
     *
     * @param transactionId the unique ID of the transaction to retrieve.
     * @return a {@link Uni} containing a {@link TransactionResponseDto} representing the transaction,
     * or empty if the transaction was not found.
     */
    public Uni<TransactionResponseDto> getTransactionById(String transactionId) {
        return transactionRepository.getTransactionById(UUID.fromString(transactionId))
                .map(transactionConverter::toDto);
    }

    /**
     * Retrieves all transactions from the repository.
     * This method queries the repository for all transactions and returns them as a list of DTOs.
     *
     * @return a {@link Multi} containing a set of {@link TransactionResponseDto}s representing all transactions.
     */
    public Multi<Set<TransactionResponseDto>> getAllTransaction() {
        return transactionRepository.getAllTransactions()
                .toMulti()
                .map(transactionConverter::toDto);
    }

    /**
     * Creates a new transaction using the provided {@link TransactionRequestDto}.
     * This method converts the DTO to a {@link Transaction} entity, saves it using the repository,
     * and returns a DTO representation of the newly created transaction.
     *
     * @param transactionRequestDto the DTO containing the data for the new transaction.
     * @return a {@link Uni} containing a {@link TransactionResponseDto} representing the created transaction.
     */
    public Uni<TransactionResponseDto> createTransaction(TransactionRequestDto transactionRequestDto) {
        return transactionRepository.saveTransaction(transactionConverter.toEntity(transactionRequestDto))
                .map(transactionConverter::toDto);
    }

    /**
     * Updates an existing transaction by its ID with the data from the provided {@link TransactionRequestDto}.
     * This method retrieves the transaction by its ID, updates it with the provided data, and saves the
     * updated transaction back to the repository. It returns a DTO representation of the updated transaction.
     *
     * @param transactionRequestDto the DTO containing the updated data for the transaction.
     * @param transactionId         the ID of the transaction to update.
     * @return a {@link Uni} containing a {@link TransactionResponseDto} representing the updated transaction.
     */
    @WithTransaction
    public Uni<TransactionResponseDto> updateTransaction(TransactionRequestDto transactionRequestDto, String transactionId) {
        return transactionRepository.getTransactionById(UUID.fromString(transactionId))
                .map(transaction -> updateTransaction(transaction, transactionRequestDto))
                .flatMap(transaction -> transactionRepository.saveTransaction(transaction)
                        .map(transactionConverter::toDto));
    }

    /**
     * Deletes a transaction by its unique ID.
     * This method removes the transaction from the repository based on the provided ID.
     *
     * @param transactionId the unique ID of the transaction to delete.
     * @return a {@link Uni} containing a {@code Boolean} indicating whether the deletion was successful.
     */
    public Uni<Boolean> deleteTransaction(String transactionId) {
        return transactionRepository.deleteTransaction(UUID.fromString(transactionId));
    }
}

