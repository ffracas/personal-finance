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
@Table(name = "RecurringExpenses")
@Getter
@Setter
public class RecurringExpense extends PanacheEntityBase {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal amount;
    @Column(length = 100)
    public String category;
    @Column(length = 20)
    public String frequency;
    @Column(name = "start_date", nullable = false)
    public LocalDate startDate;
    @Column(name = "end_date")
    public LocalDate endDate;
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
