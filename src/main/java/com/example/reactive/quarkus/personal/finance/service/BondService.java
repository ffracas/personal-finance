package com.example.reactive.quarkus.personal.finance.service;

import com.example.reactive.quarkus.personal.finance.converter.BondConverter;
import com.example.reactive.quarkus.personal.finance.model.entity.Bond;
import com.example.reactive.quarkus.personal.finance.model.request.BondRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.BondResponseDto;
import com.example.reactive.quarkus.personal.finance.repository.BondRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;
import java.util.UUID;

/**
 * {@code BondService} is a stateless application-scoped service that provides
 * operations for managing {@link Bond} entities.
 * <p>
 * It acts as an intermediary between the {@link BondRepository} for persistence
 * and the {@link BondConverter} for converting between domain entities and DTOs.
 * </p>
 *
 * <p>This service handles all core operations such as:</p>
 * <ul>
 *   <li>Creating new bonds</li>
 *   <li>Retrieving bonds by ID</li>
 *   <li>Listing all bonds</li>
 *   <li>Updating existing bonds</li>
 *   <li>Deleting bonds</li>
 * </ul>
 *
 * <p>Reactive programming is used through the use of {@link Uni} and {@link Multi}
 * from Mutiny, which allows asynchronous non-blocking operations.</p>
 *
 * <p>This service is annotated with {@link ApplicationScoped}, making it a singleton
 * within the CDI context, ideal for stateless business logic.</p>
 *
 * @author
 */
@ApplicationScoped
public final class BondService {

    private final BondRepository bondRepository;
    private final BondConverter bondConverter;

    /**
     * Constructs a {@code BondService} with required dependencies.
     *
     * @param bondRepository the repository handling persistence for bond entities
     * @param bondConverter  the converter for transforming between Bond entities and DTOs
     */
    public BondService(BondRepository bondRepository, BondConverter bondConverter) {
        this.bondRepository = bondRepository;
        this.bondConverter = bondConverter;
    }

    /**
     * Updates the fields of a {@link Bond} instance using data from a {@link BondRequestDto}.
     *
     * @param bond           the existing bond to update
     * @param bondRequestDto the DTO containing updated bond data
     * @return the updated {@code Bond} entity
     */
    private static Bond updateBond(Bond bond, BondRequestDto bondRequestDto) {
        bond.setCode(bondRequestDto.code());
        bond.setName(bondRequestDto.name());
        bond.setAnnualRate(bondRequestDto.annualRate());
        bond.setCouponType(bondRequestDto.couponType());
        bond.setCurrentValue(bondRequestDto.currentValue());
        bond.setInvestedAmount(bondRequestDto.investedAmount());
        bond.setMaturityDate(bondRequestDto.maturityDate());
        return bond;
    }

    /**
     * Creates a new bond based on the provided request data.
     *
     * @param bondRequestDto the request DTO containing bond details
     * @return a {@link Uni} emitting the created {@link BondResponseDto}
     */
    public Uni<BondResponseDto> createBond(final BondRequestDto bondRequestDto) {
        return bondRepository.createBond(bondConverter.toEntity(bondRequestDto))
                .map(bondConverter::toDto);
    }

    /**
     * Retrieves a bond by its unique identifier.
     *
     * @param bondId the UUID string of the bond to retrieve
     * @return a {@link Uni} emitting the corresponding {@link BondResponseDto}
     */
    public Uni<BondResponseDto> getBondById(final String bondId) {
        return bondRepository.getBondById(UUID.fromString(bondId))
                .map(bondConverter::toDto);
    }

    /**
     * Deletes a bond by its unique identifier.
     *
     * @param bondId the UUID string of the bond to delete
     * @return a {@link Uni} emitting {@code true} if the bond was successfully deleted,
     * {@code false} otherwise
     */
    public Uni<Boolean> deleteBond(final String bondId) {
        return bondRepository.deleteBond(UUID.fromString(bondId));
    }

    /**
     * Retrieves all bonds as a reactive stream.
     *
     * @return a {@link Multi} emitting sets of {@link BondResponseDto}, one per emission
     */
    public Multi<Set<BondResponseDto>> getAllBonds() {
        return bondRepository.getAllBonds()
                .toMulti()
                .map(bondConverter::toDto);
    }

    /**
     * Updates an existing bond identified by the given ID with new data.
     *
     * @param bondId         the UUID string of the bond to update
     * @param bondRequestDto the DTO containing updated bond data
     * @return a {@link Uni} emitting the updated {@link BondResponseDto}
     */
    @WithTransaction
    public Uni<BondResponseDto> updateBond(final String bondId, final BondRequestDto bondRequestDto) {
        return bondRepository.findById(UUID.fromString(bondId))
                .map(bond -> updateBond(bond, bondRequestDto))
                .flatMap(bondRepository::persist)
                .map(bondConverter::toDto);
    }
}

