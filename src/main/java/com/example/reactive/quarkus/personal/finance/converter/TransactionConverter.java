package com.example.reactive.quarkus.personal.finance.converter;

import com.example.reactive.quarkus.personal.finance.model.entity.Transaction;
import com.example.reactive.quarkus.personal.finance.model.entity.User;
import com.example.reactive.quarkus.personal.finance.model.request.TransactionRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.TransactionResponseDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class TransactionConverter implements Converter<TransactionRequestDto, TransactionResponseDto, Transaction> {
    @Override
    public TransactionResponseDto toDto(Transaction entity) {
        return new TransactionResponseDto(entity.getUser().getId(), entity.getId().toString(), entity.getAmount(), entity.getCategory(),
                entity.getSubCategory(), entity.getType(), entity.getTransactionDate());
    }

    @Override
    public Transaction toEntity(TransactionRequestDto dto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(dto.transactionDate());
        transaction.setAmount(dto.amount());
        transaction.setCategory(dto.category());
        transaction.setSubCategory(dto.subCategory());
        transaction.setType(dto.type().toLowerCase());
        User user = new User();
        user.setId(UUID.fromString(dto.userId()));
        transaction.setUser(user);
        return transaction;
    }

    @Override
    public Transaction toEntity(Long id, TransactionRequestDto dto) {
        return toEntity(dto);
    }
}
