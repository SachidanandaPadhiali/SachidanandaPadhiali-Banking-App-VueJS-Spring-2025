package com.banking.bank_app_java.service;

import java.util.List;

import com.banking.bank_app_java.dto.BankResponse;
import com.banking.bank_app_java.entity.Banks;

public interface BankingService {

	public BankResponse newBank(String address);
	public List<String> getAllAddresses();
	public Banks getBankIdByAddress(String address);
	public Banks getIfsc(String accountNumber);

}
