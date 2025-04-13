package com.example.reactive.quarkus.personal.finance.converter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A generic interface that defines a bidirectional conversion contract between three layers of an application:
 * - {@code DI} (Data Input): Often represents input models such as request payloads, form data, or inbound DTOs.
 * - {@code DO} (Data Output): Typically represents output models such as response DTOs or presentation-layer objects.
 * - {@code E} (Entity): Represents the core domain or persistence-layer entity, usually a JPA or database-mapped object.
 * <p>
 * This interface is a key building block in architectures that emphasize separation of concerns between the domain,
 * persistence, and API layers. It abstracts the transformation logic between:
 * <ul>
 *     <li>External API or user input → internal domain entities</li>
 *     <li>Internal domain entities → output DTOs or response payloads</li>
 * </ul>
 * <p>
 * Implementations of this interface are often used in:
 * <ul>
 *     <li>Service layers to adapt between REST controllers and JPA repositories</li>
 *     <li>Mapper classes (e.g., when not using MapStruct or ModelMapper)</li>
 *     <li>Data processing pipelines requiring strong typing between transformation stages</li>
 * </ul>
 *
 * @param <DI> the input type, such as a request DTO or form-bound model
 * @param <DO> the output type, such as a response DTO or view model
 * @param <E>  the domain entity type (persistent object)
 */
public interface Converter<DI, DO, E> {

    /**
     * Converts a domain entity ({@code E}) into a data output object ({@code DO}).
     * <p>
     * This method is typically used when returning data from the service or persistence layer to the client-facing
     * controller or presentation layer. It should map internal fields to external-facing properties.
     *
     * @param entity the entity to convert; must not be {@code null}
     * @return the corresponding DTO representation of the entity; must not be {@code null}
     * @throws NullPointerException if {@code entity} is {@code null}
     */
    DO toDto(E entity);

    /**
     * Converts a list of domain entities ({@code List<E>}) into a {@link Set} of data output objects ({@code DO}).
     * <p>
     * Internally calls {@link #toDto(Object)} for each element. Collects the results into a {@code Set} to ensure
     * uniqueness and to prevent duplicate DTO representations.
     * <p>
     * This default method is useful for batch-conversion of records retrieved from a repository.
     * <p>
     * Note: Using a {@code Set} may introduce performance tradeoffs and may change ordering of elements.
     *
     * @param entity a non-null list of entities to convert
     * @return a set of DTOs representing the input entities
     * @throws NullPointerException if {@code entity} is {@code null}
     */
    default Set<DO> toDto(List<E> entity) {
        return entity.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    /**
     * Converts a list of data input objects ({@code List<DI>}) into a {@link Set} of domain entities ({@code E}).
     * <p>
     * Internally delegates to {@link #toEntity(Object)} for each item in the input list.
     * <p>
     * Use this method when bulk-creating or updating entities based on incoming request bodies.
     * <p>
     * The default implementation returns a {@code Set}, discarding duplicates that may result from
     * identity or field-level similarities. Override this method if order preservation or duplicate
     * support is necessary (e.g., for transactional integrity or processing order).
     *
     * @param entity a non-null list of input objects to convert
     * @return a set of entities corresponding to the input data
     * @throws NullPointerException if {@code entity} is {@code null}
     */
    default Set<E> toEntity(List<DI> entity) {
        return entity.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    /**
     * Converts a data input object ({@code DI}) into its corresponding domain entity ({@code E}).
     * <p>
     * This is commonly used to transform incoming payloads (e.g., from a REST API) into domain models that
     * can be persisted, validated, or processed internally. The mapping should preserve semantic correctness
     * and enforce business rules as needed (e.g., data trimming, type normalization).
     *
     * @param dto the data input object to convert; must not be {@code null}
     * @return the corresponding entity object representing the internal model
     * @throws NullPointerException if {@code dto} is {@code null}
     */
    E toEntity(DI dto);
}
