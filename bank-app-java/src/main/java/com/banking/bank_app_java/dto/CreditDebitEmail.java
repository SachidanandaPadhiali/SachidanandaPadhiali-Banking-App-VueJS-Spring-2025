package com.banking.bank_app_java.dto;

import java.math.BigDecimal;

import com.banking.bank_app_java.entity.User;
import com.banking.bank_app_java.entity.UserBank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreditDebitEmail {
    private String recipient;
    private String subject;
    private String msgBody;
    private String attachment;
    private User user;
    private int isCredit;
    private UserBank account;
    private BigDecimal amount;
}
