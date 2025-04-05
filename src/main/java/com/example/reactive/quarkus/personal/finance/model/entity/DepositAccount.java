package com.example.reactive.quarkus.personal.finance.model.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "DepositAccounts")
public class DepositAccount extends PanacheEntityBase {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal investedAmount;
    @Column(precision = 5, scale = 2)
    public BigDecimal annualRate;
    @Column(nullable = false)
    public LocalDate startDate;
    @Column(nullable = false)
    public LocalDate endDate;
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
