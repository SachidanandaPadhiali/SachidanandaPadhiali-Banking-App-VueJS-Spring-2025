package com.banking.bank_app_java.dto;

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
    private String transactionId;
    private String transactionType;
    
    private String sourceAcc;
    private String destAcc;
    
    private BigDecimal transactionAmt;
    private String transactionStatus;
    
    private String transactionTime;
}
