package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.functional.Either;
import com.example.reactive.quarkus.personal.finance.model.entity.Bond;
import com.example.reactive.quarkus.personal.finance.model.error.BondNotFound;
import com.example.reactive.quarkus.personal.finance.model.error.BondServerError;
import com.example.reactive.quarkus.personal.finance.model.error.Error;
import com.example.reactive.quarkus.personal.finance.model.success.BondDeleteOk;
import com.example.reactive.quarkus.personal.finance.model.success.Success;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

/**
 * {@code BondRepository} is a Quarkus repository class that provides reactive access
 * to {@link Bond} entities using the Hibernate Reactive Panache API.
 * <p>
 * It extends {@link PanacheRepositoryBase}, which gives access to standard CRUD operations
 * and query capabilities for {@link Bond} entities using {@code UUID} as their primary key.
 * </p>
 *
 * <p>This repository is annotated with {@link ApplicationScoped}, meaning a single instance
 * will be maintained and injected throughout the application lifecycle. All methods are
 * annotated with {@link WithTransaction} to ensure operations run within a transactional context.</p>
 *
 * <p>All operations return {@link Uni} to enable non-blocking, asynchronous handling using
 * the Mutiny reactive programming model.</p>
 *
 * @see io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
 * @see jakarta.enterprise.context.ApplicationScoped
 * @see io.smallrye.mutiny.Uni
 */
@ApplicationScoped
public final class BondRepository implements PanacheRepositoryBase<Bond, UUID> {
    private static Uni<Either<Error, Bond>> processResponse(Uni<Bond> bondUni) {
        return bondUni
                .<Either<Error, Bond>>map(bond -> bond != null ? Either.right(bond) : Either.left(new BondNotFound(BondRepository.class.getName())))
                .onFailure()
                .recoverWithItem(throwable -> Either.left(new BondServerError(throwable.getMessage(), BondRepository.class.getName())));
    }

    /**
     * Retrieves a bond entity from the database by its UUID.
     *
     * @param bondId the unique identifier of the bond to retrieve
     * @return a {@link Uni} emitting the {@link Bond} if found, or {@code null} if not
     */
    @WithTransaction
    public Uni<Either<Error, Bond>> getBondById(UUID bondId) {
        return processResponse(findById(bondId));
    }

    /**
     * Retrieves all bond entities from the database.
     *
     * @return a {@link Uni} emitting a {@link List} of all {@link Bond} entities
     */
    @WithTransaction
    public Uni<List<Bond>> getAllBonds() {
        return findAll().list();
    }

    /**
     * Persists a new bond entity in the database.
     *
     * @param bond the {@link Bond} entity to persist
     * @return a {@link Uni} emitting the persisted {@link Bond} instance
     */
    @WithTransaction
    public Uni<Either<Error, Bond>> createBond(Bond bond) {
        return processResponse(persist(bond));
    }

    /**
     * Deletes a bond entity from the database using its UUID.
     *
     * @param bond the unique identifier of the bond to delete
     * @return a {@link Uni} emitting {@code true} if the entity was deleted, {@code false} otherwise
     */
    @WithTransaction
    public Uni<Either<Error, Success>> deleteBond(UUID bond) {
        return deleteById(bond)
                .<Either<Error, Success>>map(result -> Boolean.TRUE.equals(result) ? Either.right(new BondDeleteOk()) : Either.left(new BondNotFound(BondRepository.class.getName())))
                .onFailure()
                .recoverWithItem(throwable -> Either.left(new BondServerError(throwable.getMessage(), BondRepository.class.getName())));
    }
}
