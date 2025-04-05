package com.example.reactive.quarkus.personal.finance.model.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "FinancialGoals")
@Getter
@Setter
public class FinancialGoal extends PanacheEntityBase {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    public User user;
    @Column(name = "emergency_fund_target", precision = 15, scale = 2)
    public BigDecimal emergencyFundTarget;
    @Column(name = "investment_target", precision = 15, scale = 2)
    public BigDecimal investmentTarget;
    @Column(name = "bonds_target", precision = 15, scale = 2)
    public BigDecimal bondsTarget;
    @Column(name = "loans_target", precision = 15, scale = 2)
    public BigDecimal loansTarget;
    @Column(name = "investment_allocation", precision = 5, scale = 2)
    public BigDecimal investmentAllocation;
    @Transient
    @Column(name = "current_expenses_allocation", precision = 5, scale = 2)
    public BigDecimal currentExpensesAllocation;
    @Transient
    @Column(name = "lifestyle_expensive_allocation", precision = 5, scale = 2)
    public BigDecimal lifestyleExpensesAllocation;
    @Column(name = "emergency_months")
    public Integer emergencyMonths;
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}
