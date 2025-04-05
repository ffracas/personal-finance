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
    public User user;
    @Column(length = 100)
    public String name;
    @Column(length = 50)
    public String code;
    @Column(name = "shares_owned", precision = 15, scale = 4)
    public BigDecimal sharesOwned;
    @Column(name = "unit_price", precision = 15, scale = 4)
    public BigDecimal unitPrice;
    @Column(name = "investment_date", nullable = false)
    public LocalDate investmentDate;
    @Column(name = "current_value", precision = 15, scale = 2)
    public BigDecimal currentValue;
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
