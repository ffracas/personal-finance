package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.model.entity.User;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public final class UserRepository implements PanacheRepositoryBase<User, UUID> {
    @WithTransaction
    public Uni<User> getUserById(UUID id) {
        return findById(id);
    }

    @WithTransaction
    public Uni<List<User>> getAllUser() {
        return listAll();
    }

    @WithTransaction
    public Uni<User> saveUser(User user) {
        return persist(user)
                .onFailure()
                .invoke(Log::error);
    }

    @WithTransaction
    public Uni<Boolean> deleteUser(UUID userId) {
        return deleteById(userId);
    }
}
