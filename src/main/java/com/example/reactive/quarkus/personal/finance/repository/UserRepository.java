package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.functional.Either;
import com.example.reactive.quarkus.personal.finance.model.entity.User;
import com.example.reactive.quarkus.personal.finance.model.error.Error;
import com.example.reactive.quarkus.personal.finance.model.error.UserNotFound;
import com.example.reactive.quarkus.personal.finance.model.error.UserServerError;
import com.example.reactive.quarkus.personal.finance.model.success.Success;
import com.example.reactive.quarkus.personal.finance.model.success.UserDeleteOk;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public final class UserRepository implements PanacheRepositoryBase<User, UUID> {
    private static Uni<Either<Error, User>> processResponse(Uni<User> response) {
        return response
                .<Either<Error, User>>map(user -> user != null ? Either.right(user) : Either.left(new UserNotFound(UserRepository.class.getName())))
                .onFailure()
                .recoverWithItem(throwable -> Either.left(new UserServerError(throwable.getMessage(), UserRepository.class.getName())));
    }

    @WithTransaction
    public Uni<Either<Error, User>> getUserById(UUID id) {
        return processResponse(findById(id));
    }

    @WithTransaction
    public Uni<List<User>> getAllUser() {
        return listAll();
    }

    @WithTransaction
    public Uni<Either<Error, User>> saveUser(User user) {
        return processResponse(persist(user));
    }

    @WithTransaction
    public Uni<Either<Error, Success>> deleteUser(UUID userId) {
        return deleteById(userId)
                .<Either<Error, Success>>map(result -> Boolean.TRUE.equals(result) ? Either.right(new UserDeleteOk()) : Either.left(new UserNotFound(UserRepository.class.getName())))
                .onFailure()
                .recoverWithItem(throwable ->
                        Either.left(new UserServerError(throwable.getMessage(), UserRepository.class.getName())));
    }
}
