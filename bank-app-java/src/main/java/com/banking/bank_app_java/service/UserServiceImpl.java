package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.*;
import com.banking.bank_app_java.entity.*;
import com.banking.bank_app_java.repo.*;
import com.banking.bank_app_java.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

	@Override
	public BankResponse createAccount(UserRequests userRequests) {
		/**
		 * Check if the user is already present if present return the code and show the
		 * message in AccountUtils
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

		System.out.println("====================");
		System.out.println(getBank);
		System.out.println("====================");
		System.out.println(savedUser);
		System.out.println("====================");
		System.out.println(mapping);
		System.out.println("====================");

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
																							// create account does not
																							// have any account
																							// information
					.build();
		}
		return null;
	}

	public User validateUser(String username, String password) {
		User user = userRepo.findByEmail(username);
		return (user != null && user.getPassword().equals(password)) ? user : null;
	}
	
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
		userBankRepo.save(userBank);
		userRepo.save(userToCredit);
		/**
		 * Send Email
		 */
		EmailDetails creditEmailDetails = EmailDetails.builder().recipient(userToCredit.getEmail())
				.subject("ACCOUNT CREDITED..  " + userToCredit.getFirstName() + " " + userToCredit.getLastName())
				.msgBody("Greetings! Your account has been credited with !\n" + crRequest.getAmount()
						+ "Your current balance is \n" + userBank.getAccBalance())
				.build();
		emailService.sendEmail(creditEmailDetails);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
				.accountInfo(AccountInfo.builder().accNo(userBank.getAccNo())
						.accName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
						.accBalance(userBank.getAccBalance()).build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest drRequest) {
		// Check if Account Number is correct
		boolean isAccountExists = userBankRepo.existsByAccNo(drRequest.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		UserBank userBank = userBankRepo.findByAccNo(drRequest.getAccountNumber());
		User userToDebit = userRepo.findById(userBank.getUser().getId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		userBank.setAccBalance(userBank.getAccBalance().add(drRequest.getAmount()));
		userRepo.save(userToDebit);

		if (userBank.getAccBalance().compareTo(drRequest.getAmount()) < 0) {
			return BankResponse.builder().responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(AccountInfo.builder().accBalance(userBank.getAccBalance()).build()).build();
		}
		userBank.setAccBalance(userBank.getAccBalance().subtract(drRequest.getAmount()));
		userBankRepo.save(userBank);
		userRepo.save(userToDebit);
		/**
		 * Send Email
		 */
		EmailDetails debitEmailDetails = EmailDetails.builder().recipient(userToDebit.getEmail())
				.subject("ACCOUNT DEBITED..  " + userToDebit.getFirstName() + " " + userToDebit.getLastName())
				.msgBody("Your account has been Debited with !\n" + drRequest.getAmount() + "Your current balance is \n"
						+ userBank.getAccBalance())
				.build();
		emailService.sendEmail(debitEmailDetails);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
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
		 * Send Email
		 */
		EmailDetails debitEmailDetails = EmailDetails.builder().recipient(userFromTransfer.getEmail())
				.subject("ACCOUNT DEBITED..  " + userFromTransfer.getFirstName() + " " + userFromTransfer.getLastName())
				.msgBody("Your account has been Debited with !\n" + transferRequest.getAmount()
						+ "Your current balance is \n" + userFromBank.getAccBalance())
				.build();
		emailService.sendEmail(debitEmailDetails);

		userToBank.setAccBalance(userToBank.getAccBalance().add(transferRequest.getAmount()));
		userRepo.save(userToTransfer);
		/**
		 * Send Email
		 */
		EmailDetails creditEmailDetails = EmailDetails.builder().recipient(userToTransfer.getEmail())
				.subject("ACCOUNT CREDITED..  " + userToTransfer.getFirstName() + " " + userToTransfer.getLastName())
				.msgBody("Greetings! Your account has been credited with !\n" + transferRequest.getAmount()
						+ "Your current balance is \n" + userToBank.getAccBalance())
				.build();
		emailService.sendEmail(creditEmailDetails);

		return BankResponse.builder().responseCode(AccountUtils.BALANCE_TRANSFERRED_CODE)
				.responseMessage(AccountUtils.BALANCE_TRANSFERRED_MESSAGE)
				.accountInfo(AccountInfo.builder().accNo(userToBank.getAccNo())
						.accName(userFromTransfer.getFirstName() + " " + userFromTransfer.getLastName())
						.accBalance(userToBank.getAccBalance()).build())
				.build();
	}

}
