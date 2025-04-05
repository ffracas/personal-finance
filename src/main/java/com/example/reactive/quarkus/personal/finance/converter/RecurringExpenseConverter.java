package com.example.reactive.quarkus.personal.finance.converter;

import com.example.reactive.quarkus.personal.finance.model.entity.RecurringExpense;
import com.example.reactive.quarkus.personal.finance.model.entity.User;
import com.example.reactive.quarkus.personal.finance.model.request.RecurringExpenseRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.RecurringExpenseResponseDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class RecurringExpenseConverter implements Converter<RecurringExpenseRequestDto, RecurringExpenseResponseDto, RecurringExpense> {
    @Override
    public RecurringExpenseResponseDto toDto(RecurringExpense entity) {
        return new RecurringExpenseResponseDto(entity.getId().toString(), entity.getUser().getId().toString(),
                entity.getAmount(), entity.getCategory(), entity.getFrequency(), entity.getStartDate(), entity.getEndDate());
    }

    @Override
    public RecurringExpense toEntity(RecurringExpenseRequestDto dto) {
        RecurringExpense recurringExpense = new RecurringExpense();
        recurringExpense.setAmount(dto.amount());
        recurringExpense.setCategory(dto.category());
        recurringExpense.setFrequency(dto.frequency());
        recurringExpense.setStartDate(dto.startDate());
        recurringExpense.setEndDate(dto.endDate());
        User user = new User();
        user.setId(UUID.fromString(dto.userId()));
        recurringExpense.setUser(user);
        return recurringExpense;
    }

    @Override
    public RecurringExpense toEntity(Long id, RecurringExpenseRequestDto dto) {
        return toEntity(dto);
    }
}
