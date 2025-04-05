package com.example.reactive.quarkus.personal.finance.service;

import com.example.reactive.quarkus.personal.finance.converter.UserConverter;
import com.example.reactive.quarkus.personal.finance.model.entity.User;
import com.example.reactive.quarkus.personal.finance.model.request.UserRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.UserResponseDto;
import com.example.reactive.quarkus.personal.finance.repository.UserRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling user-related operations.
 * This class acts as an intermediary between the repository and the application logic,
 * ensuring that data is converted and processed properly.
 *
 * <p>It provides methods to:
 * <ul>
 *     <li>Retrieve a user by ID</li>
 *     <li>Retrieve all users</li>
 *     <li>Create a new user</li>
 *     <li>Update an existing user</li>
 * </ul>
 * </p>
 *
 * <p>Uses:
 * <ul>
 *     <li>{@link UserRepository} for database operations</li>
 *     <li>{@link UserConverter} for DTO to entity conversion</li>
 *     <li>{@link Uni} and {@link Multi} for reactive programming</li>
 * </ul>
 * </p>
 *
 * <p>This service is marked with {@code @ApplicationScoped},
 * meaning it is a long-lived CDI bean, initialized once per application.</p>
 */
@ApplicationScoped
public final class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    /**
     * Constructs a new {@code UserService} with the specified dependencies.
     *
     * @param userRepository the repository responsible for user persistence and retrieval
     * @param userConverter  the converter used to transform entities to DTOs and vice versa
     */
    public UserService(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    private static User updateUser(User user, UserRequestDto userRequestDto) {
        user.setEmail(userRequestDto.email());
        user.setPasswordHash(userRequestDto.password());
        user.setName(userRequestDto.name());
        return user;
    }

    /**
     * Retrieves a user by its unique ID.
     *
     * <p>This method queries the database asynchronously and converts the user entity
     * to a {@link UserResponseDto} before returning.</p>
     *
     * @param id the unique identifier of the user
     * @return a {@link Uni} emitting the user data as a {@link UserResponseDto},
     * or an empty result if no user is found
     */
    public Uni<UserResponseDto> getUserById(String id) {
        return userRepository.getUserById(UUID.fromString(id))
                .map(userConverter::toDto);
    }

    /**
     * Retrieves all users in the system.
     *
     * <p>The result is returned as a {@link Multi} containing a {@link Set} of {@link UserResponseDto}.
     * The transformation process ensures that the entity data is properly mapped to DTO format.</p>
     *
     * @return a {@link Multi} emitting a set of {@link UserResponseDto} representing all users
     */
    public Multi<Set<UserResponseDto>> getAllUser() {
        return userRepository.getAllUser()
                .toMulti()
                .map(user -> user.stream()
                        .map(userConverter::toDto)
                        .collect(Collectors.toSet()));
    }

    /**
     * Creates a new user based on the provided request data.
     *
     * <p>This method converts the input DTO to an entity, persists it in the database,
     * and then maps it back to a DTO format.</p>
     *
     * @param userRequestDto the request data containing user details
     * @return a {@link Uni} emitting the created user as a {@link UserResponseDto}
     */
    public Uni<UserResponseDto> createUser(UserRequestDto userRequestDto) {
        return userRepository.saveUser(userConverter.toEntity(userRequestDto))
                .map(userConverter::toDto);
    }

    /**
     * Updates an existing user with the given data.
     *
     * <p>This method assumes that the user exists and updates the entity in the database.
     * The updated entity is then converted back into a DTO.</p>
     *
     * @param userRequestDto the updated user data
     * @param userId
     * @return a {@link Uni} emitting the updated user as a {@link UserResponseDto}
     */
    @WithTransaction
    public Uni<UserResponseDto> updateUser(UserRequestDto userRequestDto, String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .map(user -> updateUser(user, userRequestDto))
                .flatMap(user -> userRepository.persist(user)
                        .map(userConverter::toDto));
    }

    public Uni<Boolean> deleteUser(String userId) {
        return userRepository.deleteUser(UUID.fromString(userId));
    }
}
