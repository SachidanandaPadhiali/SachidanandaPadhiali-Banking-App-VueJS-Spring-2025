package com.banking.bank_app_java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequests {
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
	private String password;

	private BigDecimal accBalance;
	//private String accStatus;
	
	private String bankAddress;
	
}
