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
public class UserResponse {

	private String firstName;
	private String lastName;
	private String gender;
	
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String pin;
	
	private String email;
	private String phoneNumber;
	
	private String accNo;
	private BigDecimal accBalance;
	private String accStatus;
	
	private String ifsc;
	private String bankAddress;
}
