package com.banking.bank_app_java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {

	private String accName;
	private BigDecimal accBalance;
	private String accNo;
}
