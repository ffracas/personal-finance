package com.example.reactive.quarkus.personal.finance.service;

import com.example.reactive.quarkus.personal.finance.converter.RecurringExpenseConverter;
import com.example.reactive.quarkus.personal.finance.model.entity.RecurringExpense;
import com.example.reactive.quarkus.personal.finance.model.request.RecurringExpenseRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.RecurringExpenseResponseDto;
import com.example.reactive.quarkus.personal.finance.repository.RecurringExpenseRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;
import java.util.UUID;

/**
 * <p>
 * The {@code RecurringExpenseService} class encapsulates the business logic for managing recurring expenses.
 * It provides asynchronous methods for retrieving, creating, and updating recurring expense data using reactive types.
 * </p>
 *
 * <p>
 * This service is responsible for orchestrating data conversion and persistence operations. It delegates data
 * access to the {@link RecurringExpenseRepository} and uses the {@link RecurringExpenseConverter} to convert between
 * entity objects and Data Transfer Objects (DTOs).
 * </p>
 *
 * <p>
 * The reactive operations in this service are implemented using Mutiny's {@link Uni} and {@link Multi} types, which
 * allow non-blocking, asynchronous processing. This design is especially beneficial in high-concurrency environments
 * where efficient resource utilization is critical.
 * </p>
 *
 * <p>
 * <strong>Key methods provided:</strong>
 * <ul>
 *   <li>{@link #getRecurringExpenseById(String)} - Retrieves a recurring expense by its unique identifier.</li>
 *   <li>{@link #getAllRecurringExpense()} - Retrieves all recurring expenses and converts them into a set of response DTOs.</li>
 *   <li>{@link #createRecurringExpense(RecurringExpenseRequestDto)} - Persists a new recurring expense based on the provided DTO.</li>
 *   <li>{@link #updateRecurringExpense(RecurringExpenseRequestDto, String)} - Updates an existing recurring expense using the provided DTO.</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class is annotated with {@code @ApplicationScoped} to indicate that it is a CDI-managed bean with a
 * lifecycle scoped to the application. Consequently, a single instance of this service is created and shared.
 * </p>
 *
 * @see RecurringExpenseRepository
 * @see RecurringExpenseConverter
 * @see RecurringExpenseRequestDto
 * @see RecurringExpenseResponseDto
 * @since 1.0
 */
@ApplicationScoped
public final class RecurringExpenseService {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final RecurringExpenseConverter recurringExpenseConverter;

    /**
     * Constructs a new {@code RecurringExpenseService} with the given repository and converter.
     *
     * @param recurringExpenseRepository the repository responsible for data access operations for recurring expenses;
     *                                   must not be {@code null}.
     * @param recurringExpenseConverter  the converter used to transform between entity objects and DTOs;
     *                                   must not be {@code null}.
     */
    public RecurringExpenseService(RecurringExpenseRepository recurringExpenseRepository,
                                   RecurringExpenseConverter recurringExpenseConverter) {
        this.recurringExpenseRepository = recurringExpenseRepository;
        this.recurringExpenseConverter = recurringExpenseConverter;
    }

    private static RecurringExpense updateRecurringExpense(RecurringExpense recurringExpense, RecurringExpenseRequestDto recurringExpenseRequestDto) {
        recurringExpense.setAmount(recurringExpenseRequestDto.amount());
        recurringExpense.setCategory(recurringExpenseRequestDto.category());
        recurringExpense.setFrequency(recurringExpenseRequestDto.frequency());
        recurringExpense.setStartDate(recurringExpenseRequestDto.startDate());
        recurringExpense.setEndDate(recurringExpenseRequestDto.endDate());
        return recurringExpense;
    }

    /**
     * Retrieves a recurring expense by its unique identifier.
     * <p>
     * This method calls the underlying repository to asynchronously fetch the recurring expense entity
     * by its ID. Once the entity is retrieved, it is transformed into a {@link RecurringExpenseResponseDto}
     * using the converter. The result is returned as a {@link Uni}, which represents a lazy asynchronous computation.
     * </p>
     *
     * @param recurringExpenseId the unique identifier of the recurring expense.
     * @return a {@code Uni<RecurringExpenseResponseDto>} that, when subscribed to, will emit the DTO representation
     * of the recurring expense corresponding to the given ID, or complete empty if not found.
     * @see RecurringExpenseRepository#getRecurringExpenseById(UUID)
     * @see RecurringExpenseConverter#toDto(RecurringExpense)
     */
    public Uni<RecurringExpenseResponseDto> getRecurringExpenseById(String recurringExpenseId) {
        return recurringExpenseRepository.getRecurringExpenseById(UUID.fromString(recurringExpenseId))
                .map(recurringExpenseConverter::toDto);
    }

    /**
     * Retrieves all recurring expenses.
     * <p>
     * This method queries the repository for all recurring expense entities and converts the resulting collection
     * into a reactive {@link Multi}. Each collection of entities is mapped to a {@code Set} of {@link RecurringExpenseResponseDto}
     * using the converter. This approach enables non-blocking streaming of data.
     * </p>
     *
     * @return a {@code Multi<Set<RecurringExpenseResponseDto>>} that, when subscribed to, emits sets of recurring expense DTOs.
     * @see RecurringExpenseRepository#getAllRecurringExpenses()
     * @see RecurringExpenseConverter#toDto(RecurringExpense)
     */
    public Multi<Set<RecurringExpenseResponseDto>> getAllRecurringExpense() {
        return recurringExpenseRepository.getAllRecurringExpenses()
                .toMulti()
                .map(recurringExpenseConverter::toDto);
    }

    /**
     * Creates a new recurring expense.
     * <p>
     * This method receives a {@link RecurringExpenseRequestDto} containing the data necessary to create a recurring expense.
     * The DTO is first converted into its corresponding entity using the converter. The repository is then invoked to persist
     * the entity asynchronously. Once the entity is saved, it is transformed back into a {@link RecurringExpenseResponseDto}.
     * The entire operation is performed in a non-blocking manner using a {@link Uni}.
     * </p>
     *
     * @param recurringExpenseRequestDto the DTO containing the recurring expense details to be created.
     * @return a {@code Uni<RecurringExpenseResponseDto>} that, upon subscription, emits the DTO representation of the newly created recurring expense.
     * @see RecurringExpenseRepository#saveRecurringExpense(RecurringExpense)
     * @see RecurringExpenseConverter#toEntity(RecurringExpenseRequestDto)
     * @see RecurringExpenseConverter#toDto(RecurringExpense)
     */
    public Uni<RecurringExpenseResponseDto> createRecurringExpense(RecurringExpenseRequestDto recurringExpenseRequestDto) {
        return recurringExpenseRepository.saveRecurringExpense(
                        recurringExpenseConverter.toEntity(recurringExpenseRequestDto))
                .map(recurringExpenseConverter::toDto);
    }

    /**
     * Updates an existing recurring expense.
     * <p>
     * This method takes a {@link RecurringExpenseRequestDto} that contains updated information for an existing recurring expense.
     * The DTO is converted into an entity, which is then passed to the repository to update the record asynchronously.
     * After the update operation, the updated entity is converted back into a {@link RecurringExpenseResponseDto}.
     * This process is carried out using a reactive {@link Uni}, ensuring that the update operation is non-blocking.
     * </p>
     *
     * @param recurringExpenseRequestDto the DTO with the updated recurring expense data.
     * @param recurringExpenseId         the unique identifier of the recurring expense.
     * @return a {@code Uni<RecurringExpenseResponseDto>} that, when subscribed to, will emit the updated recurring expense DTO.
     * @see RecurringExpenseRepository#saveRecurringExpense(RecurringExpense)
     * @see RecurringExpenseConverter#toEntity(RecurringExpenseRequestDto)
     * @see RecurringExpenseConverter#toDto(RecurringExpense)
     */
    @WithTransaction
    public Uni<RecurringExpenseResponseDto> updateRecurringExpense(RecurringExpenseRequestDto recurringExpenseRequestDto, String recurringExpenseId) {
        return recurringExpenseRepository.getRecurringExpenseById(UUID.fromString(recurringExpenseId))
                .map(recurringExpense -> updateRecurringExpense(recurringExpense, recurringExpenseRequestDto))
                .flatMap(recurringExpense -> recurringExpenseRepository.saveRecurringExpense(recurringExpense)
                        .map(recurringExpenseConverter::toDto));
    }

    /**
     * Deletes a recurring expense by its unique identifier.
     * <p>
     * This method removes the recurring expense associated with the provided {@link UUID}.
     * The deletion is performed asynchronously using the {@link Uni} type, which provides
     * a non-blocking result indicating whether the deletion was successful.
     *
     * @param recurringExpenseId the unique identifier of the recurring expense to delete.
     * @return a {@link Uni} containing a {@code Boolean} indicating whether the deletion was successful.
     * {@code true} if the recurring expense was deleted, {@code false} if no such expense was found.
     */
    public Uni<Boolean> deleteRecurringExpense(String recurringExpenseId) {
        return recurringExpenseRepository.deleteRecurringExpense(UUID.fromString(recurringExpenseId));
    }

}

