package com.banking.bank_app_java.utils;

import java.util.Random;

public class BankUtils {

    /**
     * Custom message if user is present
     */
    public static final String BANK_RECORD_EXISTS_CODE = "101";
    public static final String BANK_RECORD_EXISTS_MESSAGE = "User already exists";

    /**
     * Custom message when account is created
     */
    public static final String BANK_RECORD_CREATED_CODE = "102";
    public static final String BANK_RECORD_CREATED_MESSAGE = "Bank recorded";

    public static String generateIFSC() {
    	// Generate a random 6-digit branch code (000000–999999)
        Random random = new Random();
        int branchId = random.nextInt(1_000_000); // up to 6 digits
        String branchCode = String.format("%06d", branchId);
        return "MYBANK0" + branchCode;
    }

}
