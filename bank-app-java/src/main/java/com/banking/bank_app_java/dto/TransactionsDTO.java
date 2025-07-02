package com.banking.bank_app_java.dto;

import com.banking.bank_app_java.entity.UserBank;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsDTO {
    private BigDecimal transactionId;
    private int transactionType;
    
    private UserBank sourceAcc;
    private UserBank destAcc;
    
    private Long transactionAmt;
    private String transactionStatus;
    
    private String transactionTime;
}
