package com.banking.bank_app_java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
}
