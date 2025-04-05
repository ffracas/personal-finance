package com.example.reactive.quarkus.personal.finance.converter;

import com.example.reactive.quarkus.personal.finance.model.entity.User;
import com.example.reactive.quarkus.personal.finance.model.request.UserRequestDto;
import com.example.reactive.quarkus.personal.finance.model.response.UserResponseDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserConverter implements Converter<UserRequestDto, UserResponseDto, User> {
    @Override
    public UserResponseDto toDto(User entity) {
        return new UserResponseDto(entity.getId().toString(), entity.getName(), entity.getEmail());
    }

    @Override
    public User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.name());
        user.setPasswordHash(dto.password());
        user.setEmail(dto.email());
        return user;
    }

    @Override
    public User toEntity(Long id, UserRequestDto dto) {
        return toEntity(dto);
    }
}
