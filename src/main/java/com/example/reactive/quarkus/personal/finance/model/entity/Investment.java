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
@Table(name = "Investments")
@Getter
@Setter
public class Investment extends PanacheEntityBase {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(length = 100)
    private String name;
    @Column(length = 50)
    private String code;
    @Column(name = "shares_owned", precision = 15, scale = 4)
    private BigDecimal sharesOwned;
    @Column(name = "unit_price", precision = 15, scale = 4)
    private BigDecimal unitPrice;
    @Column(name = "investment_date", nullable = false)
    private LocalDate investmentDate;
    @Column(name = "current_value", precision = 15, scale = 2)
    private BigDecimal currentValue;
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
