package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
}
