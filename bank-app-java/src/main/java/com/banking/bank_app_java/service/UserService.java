package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.*;
import com.banking.bank_app_java.entity.User;
import com.banking.bank_app_java.entity.UserBank;
import java.util.List;

public interface UserService {
	BankResponse createAccount(UserRequests userRequest);
	User validateUser(String username, String password);
	UserBank getAccDetails(Long id);
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	String nameEnquiry(EnquiryRequest enquiryRequest);
	BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
	BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
	BankResponse transfer(Transfer transferRequest);
	BankResponse checkUserDuplicate(UserRequests userRequests);
        List<TransactionsDTO> getTransactions(String accountNumber);
}
