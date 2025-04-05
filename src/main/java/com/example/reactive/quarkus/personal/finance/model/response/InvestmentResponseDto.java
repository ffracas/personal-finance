package com.example.reactive.quarkus.personal.finance.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentResponseDto(String investmentId, String userId, String name, String code,
                                    BigDecimal sharedOwned, BigDecimal unitPrice, LocalDate investmentDate,
                                    BigDecimal currentValue) {
}
