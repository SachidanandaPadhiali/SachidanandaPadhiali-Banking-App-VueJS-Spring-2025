package com.banking.bank_app_java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.bank_app_java.dto.BankResponse;
import com.banking.bank_app_java.entity.Banks;
import com.banking.bank_app_java.repo.BankRepo;
import com.banking.bank_app_java.repo.UserBankRepo;
import com.banking.bank_app_java.utils.BankUtils;

@Service
public class BankingServiceImpl implements BankingService {

	@Autowired
	BankRepo bankRepo;
	
	@Autowired
	UserBankRepo userBankRepo;
	
	@Override
	public BankResponse newBank(String address) {
		Banks newBank = Banks.builder().address(address).ifsc(BankUtils.generateIFSC()).build();
		bankRepo.save(newBank);
				
		return BankResponse.builder().responseCode(BankUtils.BANK_RECORD_CREATED_CODE)
				.responseMessage(BankUtils.BANK_RECORD_CREATED_MESSAGE)
				.accountInfo(null)
				.build();
	}
	
	public List<String> getAllAddresses() {
        return bankRepo.findAllDistinctAddresses();
    }
	
	public Banks getBankIdByAddress(String address) {
	    return bankRepo.findByAddress(address);
	}

	@Override
	public Banks getIfsc(Long Id) {
		String ifsc = userBankRepo.searchIFSCById(Id); 
	    Banks newBank = bankRepo.findByIfsc(ifsc);
	    return newBank;	    
	}

}
