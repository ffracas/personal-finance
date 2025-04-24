package com.example.reactive.quarkus.personal.finance.model.error;

import java.time.LocalDateTime;

public record UserNotFound(String message, LocalDateTime timeStamp, String classHappen) implements UserError {
    public UserNotFound(String classHappen) {
        this("User not found", LocalDateTime.now(), classHappen);
    }
}
