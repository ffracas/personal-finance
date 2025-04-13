package com.example.reactive.quarkus.personal.finance.repository;

import com.example.reactive.quarkus.personal.finance.model.entity.DepositAccount;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

public class DepositAccountRepository implements PanacheRepository<DepositAccount> {
    public Uni<Boolean> deleteDepositAccountById(UUID uuid) {
        return null;
    }

    public Uni<DepositAccount> saveDepositAccount(Object depositAccount) {
        return null;
    }

    public Uni<DepositAccount> getDepositAccountById(UUID uuid) {
        return null;
    }

    public Uni<List<DepositAccount>> getAllDepositAccount() {
        return null;
    }
}
