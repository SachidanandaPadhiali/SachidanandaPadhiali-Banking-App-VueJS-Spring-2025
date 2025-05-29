package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.*;
import com.banking.bank_app_java.entity.User;

public interface UserService {

	
	BankResponse createAccount(UserRequests userRequest);
	public User validateUser(String username, String password);
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	String nameEnquiry(EnquiryRequest enquiryRequest);
	BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
	BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
	BankResponse transfer(Transfer transferRequest);

}
