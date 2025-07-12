package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.*;
import com.banking.bank_app_java.entity.*;
import com.banking.bank_app_java.repo.*;
import com.banking.bank_app_java.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    BankRepo bankRepo;

    @Autowired
    UserBankRepo userBankRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    private UserBankRepo mappingRepo;

    @Autowired
    private TransactionsRepo transactionsRepo;

    @Override
    public BankResponse createAccount(UserRequests userRequests) {
        /**
         * Check if the user is already present if present return the code and
         * show the message in AccountUtils
         */
        if (userRepo.existsByEmail(userRequests.getEmail())) {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null) // the user who wanted to
                    // create account does not
                    // have any account
                    // information
                    .build();
        }
        /**
         * Creating an account - saving a new user into the database
         */
        User newUser = User.builder().firstName(userRequests.getFirstName()).lastName(userRequests.getLastName())
                .gender(userRequests.getGender()).addressLine1(userRequests.getAddressLine1())
                .addressLine2(userRequests.getAddressLine2()).city(userRequests.getCity())
                .state(userRequests.getState()).pin(userRequests.getPin()).email(userRequests.getEmail())
                .phoneNumber(String.valueOf(userRequests.getPhoneNumber())).password(userRequests.getPassword())
                .build();
        /**
         * Save the created User
         */
        User savedUser = userRepo.save(newUser);
        Banks getBank = bankRepo.findByAddress(userRequests.getBankAddress());

        UserBank mapping = UserBank.builder().user(savedUser).bank(getBank).accNo(AccountUtils.generateAccountNumber())
                .accBalance(BigDecimal.ZERO).accStatus("ACTIVE").build();

        mappingRepo.save(mapping);
        /**
         * Send Email
         */
        EmailDetails emailDetails = EmailDetails.builder().recipient(userRequests.getEmail())
                .subject("🎉 ACCOUNT CREATED.. " + userRequests.getFirstName() + " " + userRequests.getLastName())
                .savedUser(savedUser).savedUserBank(mapping).build();
        emailService.sendSignUpEmail(emailDetails);

        return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(AccountInfo.builder().accBalance(mapping.getAccBalance()).accNo(mapping.getAccNo())
                        .accName(savedUser.getFirstName() + " " + savedUser.getLastName()).build())
                .build();
    }

    @Override
    public BankResponse checkUserDuplicate(UserRequests userRequests) {
        if (userRepo.existsByEmail(userRequests.getEmail())) {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null) // the user who wanted to
                    // information
                    .build();
        }
        return null;
    }

    @Override
    public User validateUser(String username, String password) {
        User user = userRepo.findByEmail(username);
        if (user != null) {
            user.setLastLogIn(LocalDateTime.now());
            userRepo.save(user);
        }
        return (user != null && user.getPassword().equals(password)) ? user : null;
    }

    @Override
    public UserBank getAccDetails(Long id) {
        return userBankRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest bRequest) {
        // Check if Account Number is correct
        boolean isAccountExists = userBankRepo.existsByAccNo(bRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE).accountInfo(null).build();
        }
        UserBank userBank = userBankRepo.findByAccNo(bRequest.getAccountNumber());
        User userAccFound = userRepo.findById(userBank.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return BankResponse.builder()
                .accountInfo(AccountInfo.builder().accNo(userBank.getAccNo())
                        .accName(userAccFound.getFirstName() + " " + userAccFound.getLastName())
                        .accBalance(userBank.getAccBalance()).build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest nRequest) {
        // Check if Account Number is correct
        boolean isAccountExists = userBankRepo.existsByAccNo(nRequest.getAccountNumber());
        if (!isAccountExists) {
            return AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE;
        }
        UserBank userBank = userBankRepo.findByAccNo(nRequest.getAccountNumber());
        User userAccFound = userRepo.findById(userBank.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ("Account Name: " + userAccFound.getFirstName() + " " + userAccFound.getLastName());
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest crRequest) {
        // Check if Account Number is correct
        boolean isAccountExists = userBankRepo.existsByAccNo(crRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE).accountInfo(null).build();
        }
        UserBank userBank = userBankRepo.findByAccNo(crRequest.getAccountNumber());
        User userToCredit = userRepo.findById(userBank.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userBank.setAccBalance(userBank.getAccBalance().add(crRequest.getAmount()));

        Transactions newTransaction = new Transactions();
        newTransaction.setTransactionId(BankUtils.generateTransactionId());
        newTransaction.setSourceAcc(userBank);
        newTransaction.setDestAcc(userBank);
        newTransaction.setTransactionAmt(crRequest.getAmount());
        newTransaction.setTransactionTime(LocalDateTime.now());
        newTransaction.setTransactionType(0);
        newTransaction.setTransactionStatus("COMPLETED");
        newTransaction.setToUpdatedBal(userBank.getAccBalance());

        transactionsRepo.save(newTransaction);
        userBankRepo.save(userBank);
        userRepo.save(userToCredit);
        /**
         * Send Email
         */
        CreditDebitEmail emailDetails = CreditDebitEmail.builder().recipient(userToCredit.getEmail())
                .subject("🎉 ACCOUNT CREDITED..  " + userToCredit.getFirstName() + " " + userToCredit.getLastName())
                .user(userToCredit).account(userBank).isCredit(1).amount(crRequest.getAmount()).build();
        emailService.sendBalanceUpdateEmail(emailDetails);

        return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(AccountInfo.builder().accNo(userBank.getAccNo())
                        .accName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accBalance(userBank.getAccBalance()).build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest drRequest) {
        // Check if Account Number is correct3
        boolean isAccountExists = userBankRepo.existsByAccNo(drRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE).accountInfo(null).build();
        }
        UserBank userBank = userBankRepo.findByAccNo(drRequest.getAccountNumber());
        User userToDebit = userRepo.findById(userBank.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userBank.getAccBalance().compareTo(drRequest.getAmount()) < 0) {
            return BankResponse.builder().responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(AccountInfo.builder().accBalance(userBank.getAccBalance()).build()).build();
        }

        userBank.setAccBalance(userBank.getAccBalance().subtract(drRequest.getAmount()));

        Transactions newTransaction = new Transactions();
        newTransaction.setTransactionId(BankUtils.generateTransactionId());
        newTransaction.setSourceAcc(userBank);
        newTransaction.setDestAcc(userBank);
        newTransaction.setTransactionAmt(drRequest.getAmount());
        newTransaction.setTransactionTime(LocalDateTime.now());
        newTransaction.setTransactionType(2);
        newTransaction.setTransactionStatus("COMPLETED");
        newTransaction.setFromUpdatedBal(userBank.getAccBalance());

        transactionsRepo.save(newTransaction);
        userBankRepo.save(userBank);
        userRepo.save(userToDebit);
        /**
         * Send Email
         */
        CreditDebitEmail emailDetails = CreditDebitEmail.builder().recipient(userToDebit.getEmail())
                .subject("My Bank Balance Update..  " + userToDebit.getFirstName() + " " + userToDebit.getLastName())
                .user(userToDebit).account(userBank).isCredit(0).amount(drRequest.getAmount()).build();
        emailService.sendBalanceUpdateEmail(emailDetails);

        return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(AccountInfo.builder().accNo(userBank.getAccNo())
                        .accName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                        .accBalance(userBank.getAccBalance()).build())
                .build();
    }

    @Override
    public BankResponse transfer(Transfer transferRequest) {
        boolean isSourceAccountExists = userBankRepo.existsByAccNo(transferRequest.getSourceAccount());
        boolean isDestinationAccountExists = userBankRepo.existsByAccNo(transferRequest.getDestinationAccount());
        if (!isSourceAccountExists || !isDestinationAccountExists) {
            return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE).accountInfo(null).build();
        }
        UserBank userFromBank = userBankRepo.findByAccNo(transferRequest.getSourceAccount());
        User userFromTransfer = userRepo.findById(userFromBank.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserBank userToBank = userBankRepo.findByAccNo(transferRequest.getDestinationAccount());
        User userToTransfer = userRepo.findById(userToBank.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userFromBank.getAccBalance().compareTo(transferRequest.getAmount()) < 0) {
            return BankResponse.builder().responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(AccountInfo.builder().accBalance(userFromBank.getAccBalance()).build()).build();
        }
        userFromBank.setAccBalance(userFromBank.getAccBalance().subtract(transferRequest.getAmount()));

        userBankRepo.save(userFromBank);
        userRepo.save(userFromTransfer);
        /**
         * Send Debit Email
         */
        CreditDebitEmail debitEmailDetails = CreditDebitEmail.builder().recipient(userFromTransfer.getEmail())
                .subject("My Bank Balance Update..  " + userFromTransfer.getFirstName() + " " + userFromTransfer.getLastName())
                .user(userFromTransfer).account(userFromBank).isCredit(0).amount(transferRequest.getAmount()).build();
        emailService.sendBalanceUpdateEmail(debitEmailDetails);

        userToBank.setAccBalance(userToBank.getAccBalance().add(transferRequest.getAmount()));
        userBankRepo.save(userToBank);
        userRepo.save(userToTransfer);

        /**
         * Send Credit Email
         */
        CreditDebitEmail creditEmailDetails = CreditDebitEmail.builder().recipient(userToTransfer.getEmail())
                .subject("🎉 ACCOUNT CREDITED..  " + userToTransfer.getFirstName() + " " + userToTransfer.getLastName())
                .user(userToTransfer).account(userToBank).isCredit(1).amount(transferRequest.getAmount()).build();
        emailService.sendBalanceUpdateEmail(creditEmailDetails);

        Transactions newTransaction = new Transactions();
        newTransaction.setTransactionId(BankUtils.generateTransactionId());
        newTransaction.setSourceAcc(userFromBank);
        newTransaction.setDestAcc(userToBank);
        newTransaction.setTransactionAmt(transferRequest.getAmount());
        newTransaction.setTransactionTime(LocalDateTime.now());
        newTransaction.setTransactionType(1);
        newTransaction.setTransactionStatus("COMPLETED");
        newTransaction.setFromUpdatedBal(userFromBank.getAccBalance());
        newTransaction.setToUpdatedBal(userToBank.getAccBalance());
        transactionsRepo.save(newTransaction);

        return BankResponse.builder().responseCode(AccountUtils.BALANCE_TRANSFERRED_CODE)
                .responseMessage(AccountUtils.BALANCE_TRANSFERRED_MESSAGE)
                .accountInfo(AccountInfo.builder().accNo(userToBank.getAccNo())
                        .accName(userFromTransfer.getFirstName() + " " + userFromTransfer.getLastName())
                        .accBalance(userToBank.getAccBalance()).build())
                .build();
    }

    @Override
    public List<TransactionsDTO> getTransactions(String accountNumber) {
        List<Transactions> userTransactions = transactionsRepo.findTop30BySourceAcc_AccNoOrDestAcc_AccNoOrderByTransactionTimeDesc(
                accountNumber,
                accountNumber
        );

        //Convert all the transactions into transactionDTO and return the result array
        List<TransactionsDTO> userTxs = new ArrayList<>();

        for (Transactions txs : userTransactions) {

            String trType = "";
            switch (txs.getTransactionType()) {
                case 0:
                    trType = "DEPOSIT";
                    break;
                case 1:
                    if (txs.getSourceAcc().getAccNo().equals(accountNumber)) {
                        trType = "DEBIT";
                    } else if (txs.getDestAcc().getAccNo().equals(accountNumber)) {
                        trType = "CREDIT";
                    }
                    break;
                case 2:
                    trType = "WITHDRAW";
                    break;
                default:
                    throw new AssertionError();
            }
            TransactionsDTO bankTransaction = TransactionsDTO.builder().transactionId(txs.getTransactionId())
                    .transactionAmt(txs.getTransactionAmt())
                    .transactionStatus(txs.getTransactionStatus())
                    .transactionType(trType)
                    .transactionTime(txs.getTransactionTime().toString())
                    .sourceAcc(txs.getSourceAcc().getAccNo())
                    .destAcc(txs.getDestAcc().getAccNo())
                    .fromUpdatedBal(txs.getFromUpdatedBal())
                    .toUpdatedBal(txs.getToUpdatedBal())
                    .build();

            userTxs.add(bankTransaction);
        }
        return userTxs;
    }
}
