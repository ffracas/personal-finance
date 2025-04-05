package com.example.reactive.quarkus.personal.finance.model.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User extends PanacheEntityBase {
    @Column(length = 100, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    public String email;
    @Column(name = "password_hash", nullable = false)
    public String passwordHash;
    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public LocalDateTime creationDate = LocalDateTime.now();
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
