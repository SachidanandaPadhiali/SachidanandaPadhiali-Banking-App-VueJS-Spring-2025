package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.*;
import com.banking.bank_app_java.entity.User;
import com.banking.bank_app_java.repo.UserRepo;
import com.banking.bank_app_java.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepo userRepo;

	@Autowired
	EmailService emailService;

	@Override
	public BankResponse createAccount(UserRequests userRequests) {
		/**
		 * Check if the user is already present
		 * if present return the code and show the message in AccountUtils
		 */
		if(userRepo.existsByEmail(userRequests.getEmail())) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null) //the user who wanted to create account does not have any account information
					.build();
		}
		/**
		 * Creating an account - saving a new user into the database
		 */
		User newUser = User.builder()
				.firstName(userRequests.getFirstName())
				.lastName(userRequests.getLastName())
				.gender(userRequests.getGender())
				.addressLine1(userRequests.getAddressLine1())
				.addressLine2(userRequests.getAddressLine2())
				.city(userRequests.getCity())
				.state(userRequests.getState())
				.pin(userRequests.getPin())
				.email(userRequests.getEmail())
				.phoneNumber(String.valueOf(userRequests.getPhoneNumber()))
				.password(userRequests.getPassword())
				.accNo(AccountUtils.generateAccountNumber())
				.accBalance(BigDecimal.ZERO)
				.accStatus("ACTIVE")
				.build();
		/**
		 * Save the created User
		 */
		User savedUser = userRepo.save(newUser);
		/**
		 * Send Email
		 */
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(userRequests.getEmail())
				.subject("🎉 ACCOUNT CREATED.. "+userRequests.getFirstName() + " " + userRequests.getLastName())
				.savedUser(savedUser)
				.build();
		emailService.sendSignUpEmail(emailDetails);

		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accBalance(savedUser.getAccBalance())
						.accNo(savedUser.getAccNo())
						.accName(savedUser.getFirstName()+" "+savedUser.getLastName())
						.build())
				.build();
    }

	@Override
	public BankResponse checkUserDuplicate(UserRequests userRequests) {
		if(userRepo.existsByEmail(userRequests.getEmail())) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null) //the user who wanted to create account does not have any account information
					.build();
		}
		return null;
	}

	public User validateUser(String username, String password) {
		User user = userRepo.findByEmail(username);
		return (user != null && user.getPassword().equals(password)) ? user : null;
	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest bRequest) {
		//Check if Account Number is correct
		boolean isAccountExists = userRepo.existsByAccNo(bRequest.getAccountNumber());
		if(!isAccountExists) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userAccFound = userRepo.findByAccNo(bRequest.getAccountNumber());
		return BankResponse.builder()
				.accountInfo(AccountInfo.builder()
						.accNo(userAccFound.getAccNo())
						.accName(userAccFound.getFirstName()+" "+userAccFound.getLastName())
						.accBalance(userAccFound.getAccBalance())
						.build())
				.build();
    }

	@Override
	public String nameEnquiry(EnquiryRequest nRequest) {
		//Check if Account Number is correct
		boolean isAccountExists = userRepo.existsByAccNo(nRequest.getAccountNumber());
		if(!isAccountExists) {
			return AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE;
		}
		User userAccFound = userRepo.findByAccNo(nRequest.getAccountNumber());
		return ("Account Name: "+ userAccFound.getFirstName()+" "+userAccFound.getLastName());
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest crRequest) {
		//Check if Account Number is correct
		boolean isAccountExists = userRepo.existsByAccNo(crRequest.getAccountNumber());
		if(!isAccountExists) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToCredit = userRepo.findByAccNo(crRequest.getAccountNumber());
		userToCredit.setAccBalance(userToCredit.getAccBalance().add(crRequest.getAmount()));
		userRepo.save(userToCredit);
		/**
		 * Send Email
		 */
		EmailDetails creditEmailDetails = EmailDetails.builder()
				.recipient(userToCredit.getEmail())
				.subject("ACCOUNT CREDITED..  "+userToCredit.getFirstName() + " " + userToCredit.getLastName())
				.msgBody("Greetings! Your account has been credited with !\n" + crRequest.getAmount() +
						"Your current balance is \n" + userToCredit.getAccBalance()
				)
				.build();
		emailService.sendEmail(creditEmailDetails);

		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accNo(userToCredit.getAccNo())
						.accName(userToCredit.getFirstName()+" "+userToCredit.getLastName())
						.accBalance(userToCredit.getAccBalance())
						.build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest drRequest) {
		//Check if Account Number is correct
		boolean isAccountExists = userRepo.existsByAccNo(drRequest.getAccountNumber());
		if(!isAccountExists) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToDebit = userRepo.findByAccNo(drRequest.getAccountNumber());

		if (userToDebit.getAccBalance().compareTo(drRequest.getAmount()) < 0) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(AccountInfo.builder()
							.accBalance(userToDebit.getAccBalance())
							.build())
					.build();
		}
		userToDebit.setAccBalance(userToDebit.getAccBalance().subtract(drRequest.getAmount()));
		userRepo.save(userToDebit);
		/**
		 * Send Email
		 */
		EmailDetails debitEmailDetails = EmailDetails.builder()
				.recipient(userToDebit.getEmail())
				.subject("ACCOUNT DEBITED..  "+userToDebit.getFirstName() + " " + userToDebit.getLastName())
				.msgBody("Your account has been Debited with !\n" + drRequest.getAmount() +
						"Your current balance is \n" + userToDebit.getAccBalance()
				)
				.build();
		emailService.sendEmail(debitEmailDetails);

		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accNo(userToDebit.getAccNo())
						.accName(userToDebit.getFirstName()+" "+userToDebit.getLastName())
						.accBalance(userToDebit.getAccBalance())
						.build())
				.build();
	}

	@Override
	public BankResponse transfer(Transfer transferRequest) {
		boolean isSourceAccountExists = userRepo.existsByAccNo(transferRequest.getSourceAccount());
		boolean isDestinationAccountExists = userRepo.existsByAccNo(transferRequest.getDestinationAccount());
		if(!isSourceAccountExists || !isDestinationAccountExists) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userFromTransfer = userRepo.findByAccNo(transferRequest.getSourceAccount());
		User userToTransfer = userRepo.findByAccNo(transferRequest.getDestinationAccount());

		if (userFromTransfer.getAccBalance().compareTo(transferRequest.getAmount()) < 0) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(AccountInfo.builder()
							.accBalance(userFromTransfer.getAccBalance())
							.build())
					.build();
		}
		userFromTransfer.setAccBalance(userFromTransfer.getAccBalance().subtract(transferRequest.getAmount()));
		userRepo.save(userFromTransfer);
		/**
		 * Send Email
		 */
		EmailDetails debitEmailDetails = EmailDetails.builder()
				.recipient(userFromTransfer.getEmail())
				.subject("ACCOUNT DEBITED..  "+userFromTransfer.getFirstName() + " " + userFromTransfer.getLastName())
				.msgBody("Your account has been Debited with !\n" + transferRequest.getAmount() +
						"Your current balance is \n" + userFromTransfer.getAccBalance()
				)
				.build();
		emailService.sendEmail(debitEmailDetails);

		userToTransfer.setAccBalance(userToTransfer.getAccBalance().add(transferRequest.getAmount()));
		userRepo.save(userToTransfer);
		/**
		 * Send Email
		 */
		EmailDetails creditEmailDetails = EmailDetails.builder()
				.recipient(userToTransfer.getEmail())
				.subject("ACCOUNT CREDITED..  "+userToTransfer.getFirstName() + " " + userToTransfer.getLastName())
				.msgBody("Greetings! Your account has been credited with !\n" + transferRequest.getAmount() +
						"Your current balance is \n" + userToTransfer.getAccBalance()
				)
				.build();
		emailService.sendEmail(creditEmailDetails);

		return BankResponse.builder()
				.responseCode(AccountUtils.BALANCE_TRANSFERRED_CODE)
				.responseMessage(AccountUtils.BALANCE_TRANSFERRED_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accNo(userFromTransfer.getAccNo())
						.accName(userFromTransfer.getFirstName()+" "+userFromTransfer.getLastName())
						.accBalance(userFromTransfer.getAccBalance())
						.build())
				.build();
	}

}
