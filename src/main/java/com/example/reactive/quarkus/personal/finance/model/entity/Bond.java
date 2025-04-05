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
@Table(name = "Bonds")
@Getter
@Setter
public class Bond extends PanacheEntityBase {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    @Column(length = 100)
    public String name;
    @Column(length = 50)
    public String code;
    @Column(name = "invested_amount", precision = 15, scale = 2, nullable = false)
    public BigDecimal investedAmount;
    @Column(name = "annual_rate", precision = 5, scale = 2)
    public BigDecimal annualRate;
    @Column(name = "maturity_date", nullable = false)
    public LocalDate maturityDate;
    @Column(name = "coupon_type", length = 50)
    public String couponType;
    @Column(name = "current_value", precision = 15, scale = 2)
    public BigDecimal currentValue;
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
