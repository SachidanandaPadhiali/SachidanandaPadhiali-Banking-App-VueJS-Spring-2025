package com.banking.bank_app_java.utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class AccountUtils {

    /**
     * Custom message if user is present
     */
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "User already exists";

    /**
     * Custom message when account is created
     */
    public static final String ACCOUNT_CREATED_CODE = "002";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account Created";

    /**
     * Custom message if account is not present
     */
    public static final String ACCOUNT_DOES_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "Account Number does not exists";

    /**
     * Custom message credited
     */
    public static final String ACCOUNT_CREDITED_CODE = "011";
    public static final String ACCOUNT_CREDITED_MESSAGE = "Balance credited";

    /**
     * Custom message debited
     */
    public static final String ACCOUNT_DEBITED_CODE = "012";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Balance debited";

    /**
     * Custom message Balance Insufficient
     */
    public static final String INSUFFICIENT_BALANCE_CODE = "013";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";

    /**
     * Custom message transferred
     */
    public static final String BALANCE_TRANSFERRED_CODE = "014";
    public static final String BALANCE_TRANSFERRED_MESSAGE = "Amount Transferred";

    public static String generateAccountNumber() {
        /**
         * YY+MM+random6digit
         */
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyDDDww");
        String formatted = today.format(formatter);

        int min = 100000;
        int max = 999999;

        //Generate a random number
        String randomNum = String.valueOf(min + (int)(Math.random() * (max - min)));

        return new StringBuilder().append(formatted).append(randomNum).toString();
    }

}
