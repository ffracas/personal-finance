package com.example.reactive.quarkus.personal.finance.service;

import com.example.reactive.quarkus.personal.finance.converter.InvestmentConverter;
import com.example.reactive.quarkus.personal.finance.functional.Either;
import com.example.reactive.quarkus.personal.finance.model.entity.Investment;
import com.example.reactive.quarkus.personal.finance.model.error.Error;
import com.example.reactive.quarkus.personal.finance.model.request.InvestmentRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.InvestmentResponseDto;
import com.example.reactive.quarkus.personal.finance.model.success.Success;
import com.example.reactive.quarkus.personal.finance.repository.InvestmentRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;
import java.util.UUID;

import static com.example.reactive.quarkus.personal.finance.utility.UtilMutiny.createUniError;
import static com.example.reactive.quarkus.personal.finance.utility.UtilMutiny.startUniFromItem;

/**
 * {@code InvestmentService} provides business logic for handling investment-related operations.
 * <p>
 * It acts as a service layer between the controller and repository, managing data conversion,
 * persistence, retrieval, and updates of {@link Investment} entities.
 * <p>
 * This class is annotated with {@link ApplicationScoped}, making it a CDI bean with a lifecycle
 * that lasts for the duration of the application.
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Persist new investments to the database</li>
 *     <li>Retrieve individual or all investments</li>
 *     <li>Update existing investments</li>
 *     <li>Delete investments</li>
 *     <li>Convert between domain entities and DTOs</li>
 * </ul>
 *
 * @see InvestmentRepository
 * @see InvestmentConverter
 * @see Investment
 * @see InvestmentRequestDto
 * @see InvestmentResponseDto
 */
@ApplicationScoped
public final class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final InvestmentConverter investmentConverter;

    /**
     * Constructs an instance of {@code InvestmentService}.
     *
     * @param investmentRepository the repository handling persistence logic for investments
     * @param investmentConverter  the converter responsible for transforming between DTOs and entities
     */
    public InvestmentService(InvestmentRepository investmentRepository, InvestmentConverter investmentConverter) {
        this.investmentRepository = investmentRepository;
        this.investmentConverter = investmentConverter;
    }

    /**
     * Updates an existing {@link Investment} entity with the values from the provided {@link InvestmentRequestDto}.
     *
     * @param investment           the investment entity to update
     * @param investmentRequestDto the DTO containing new values
     * @return the updated {@link Investment} entity
     */
    private static Investment updateInvestment(Investment investment, InvestmentRequestDto investmentRequestDto) {
        investment.setCode(investmentRequestDto.code());
        investment.setName(investmentRequestDto.name());
        investment.setInvestmentDate(investmentRequestDto.investmentDate());
        investment.setCurrentValue(investmentRequestDto.currentValue());
        investment.setSharesOwned(investmentRequestDto.sharedOwned());
        investment.setUnitPrice(investmentRequestDto.unitPrice());
        return investment;
    }

    /**
     * Creates and persists a new investment.
     *
     * @param investmentRequestDto the investment details to be saved
     * @return a {@link Uni} emitting the saved investment as a {@link InvestmentResponseDto}
     */
    public Uni<Either<Error, InvestmentResponseDto>> createInvestment(InvestmentRequestDto investmentRequestDto) {
        return startUniFromItem(investmentRequestDto)
                .flatMap(investmentRequestDto1 -> investmentRepository.saveInvestment(investmentConverter.toEntity(investmentRequestDto1))
                        .map(either -> either.map(investmentConverter::toDto)));
    }

    /**
     * Retrieves a specific investment by its unique identifier.
     *
     * @param investmentId the UUID of the investment (as a String)
     * @return a {@link Uni} emitting the corresponding {@link InvestmentResponseDto} if found,
     * or an empty result if not found
     */
    public Uni<Either<Error, InvestmentResponseDto>> getInvestmentById(String investmentId) {
        return startUniFromItem(investmentId)
                .map(UUID::fromString)
                .flatMap(uuid -> investmentRepository.getInvestmentById(uuid)
                        .map(either -> either.map(investmentConverter::toDto)));
    }

    /**
     * Retrieves all investments as a reactive stream.
     *
     * @return a {@link Multi} emitting sets of {@link InvestmentResponseDto} instances representing all investments
     */

    public Multi<Set<InvestmentResponseDto>> getAllInvestments() {
        return investmentRepository.getAllInvestment()
                .toMulti()
                .map(investmentConverter::toDto);
    }

    /**
     * Deletes an investment by its unique identifier.
     *
     * @param investmentId the UUID of the investment to delete (as a String)
     * @return a {@link Uni} emitting {@code true} if deletion was successful, {@code false} otherwise
     */
    public Uni<Either<Error, Success>> deleteInvestmentById(String investmentId) {
        return startUniFromItem(investmentId)
                .map(UUID::fromString)
                .flatMap(uuid -> investmentRepository.deleteInvestment(UUID.fromString(investmentId)));
    }

    /**
     * Updates an existing investment with new details.
     * <p>
     * The operation is transactional to ensure atomic updates.
     *
     * @param investmentRequestDto the updated investment data
     * @param investmentId         the UUID of the investment to update (as a String)
     * @return a {@link Uni} emitting the updated {@link InvestmentResponseDto}
     */
    @WithTransaction
    public Uni<Either<Error, InvestmentResponseDto>> updateInvestment(InvestmentRequestDto investmentRequestDto, String investmentId) {
        return startUniFromItem(investmentId)
                .map(UUID::fromString)
                .flatMap(investmentRepository::getInvestmentById)
                .map(errorInvestmentEither -> errorInvestmentEither.map(investment -> updateInvestment(investment, investmentRequestDto)))
                .flatMap(errorInvestmentEither -> errorInvestmentEither.fold(error -> createUniError(errorInvestmentEither),
                        investment -> investmentRepository.saveInvestment(investment)
                                .map(errorInvestmentEither1 -> errorInvestmentEither1
                                        .map(investmentConverter::toDto))));
    }
}
