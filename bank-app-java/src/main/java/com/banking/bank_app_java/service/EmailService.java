package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.CreditDebitEmail;
import com.banking.bank_app_java.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
    void sendSignUpEmail(EmailDetails emailDetails);
    void sendBalanceUpdateEmail(CreditDebitEmail emailDetails);
}
