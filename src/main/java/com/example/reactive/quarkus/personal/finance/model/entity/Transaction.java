package com.example.reactive.quarkus.personal.finance.model.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Transactions")
@Getter
@Setter
public class Transaction extends PanacheEntityBase {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal amount;
    @Column(length = 100)
    public String category;
    @Column(name = "sub_category", length = 100)
    public String subCategory;
    @Column(length = 10, nullable = false)
    public String type; // Consider using an enum for 'income' and 'expense'
    @Column(name = "transaction_date", nullable = false)
    public LocalDate transactionDate;
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
