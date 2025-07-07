package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.*;
import com.banking.bank_app_java.entity.*;
import java.util.List;

public interface UserService {
	BankResponse createAccount(UserRequests userRequest);
	BankResponse checkUserDuplicate(UserRequests userRequests);
	User validateUser(String username, String password);
	UserBank getAccDetails(Long id);
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	String nameEnquiry(EnquiryRequest enquiryRequest);
	BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
	BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
	BankResponse transfer(Transfer transferRequest);
        List<TransactionsDTO> getTransactions(String accountNumber);
}
