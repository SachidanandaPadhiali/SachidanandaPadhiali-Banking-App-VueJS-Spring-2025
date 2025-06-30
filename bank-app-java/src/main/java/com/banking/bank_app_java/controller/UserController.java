package com.banking.bank_app_java.controller;

import com.banking.bank_app_java.dto.*;
import com.banking.bank_app_java.entity.Banks;
import com.banking.bank_app_java.entity.User;
import com.banking.bank_app_java.entity.UserBank;
import com.banking.bank_app_java.service.BankingService;
import com.banking.bank_app_java.service.UserService;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://192.168.1.4.nip.io:8080",
        allowedHeaders = "*",
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

	@Autowired
	BankingService bankingService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequests userRequests) {
        try {
            User user = userService.validateUser(userRequests.getEmail(), userRequests.getPassword());
            UserBank accdetails = userService.getAccDetails(user.getId());
            
            UserResponse response = new UserResponse();
			response.setFirstName(user.getFirstName());
			response.setLastName(user.getLastName());
			response.setEmail(user.getEmail());
			response.setGender(user.getGender());
			response.setAddressLine1(user.getAddressLine1());
			response.setAddressLine2(user.getAddressLine2());
			response.setCity(user.getCity());
			response.setState(user.getState());
			response.setPin(user.getPin());
			
			response.setEmail(user.getEmail());
			response.setPhoneNumber(user.getPhoneNumber());
                        response.setLastLogIn(user.getLastLogIn().toString());
			
			response.setAccNo(accdetails.getAccNo());
			response.setAccBalance(accdetails.getAccBalance());
			response.setAccStatus(accdetails.getAccStatus());
			response.setIfsc(accdetails.getBank().getIfsc());
			response.setBankAddress(accdetails.getBank().getAddress());
			
			return ResponseEntity.ok(response);
        }catch (Exception ex) {
            // Log the full stack trace to console/logs:
            ex.printStackTrace();
            // Return a 500 with the exception message (for debugging only):
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + ex.getMessage());
        }
    }

    @PostMapping("/user")
    public BankResponse createAccount(@RequestBody UserRequests userRequests) {
        return userService.createAccount(userRequests);
    }

    @GetMapping("/checkuser")
    public BankResponse checkUserDuplicate(@RequestParam String email) {
    	UserRequests userRequests = new UserRequests();
    	userRequests.setEmail(email);
        return userService.checkUserDuplicate(userRequests);
    }

    @GetMapping("/user/BalanceEnquiry")
    public BankResponse accountBalanceEnquiry(@RequestParam String accountNumber) {
        EnquiryRequest enquiryRequest = new EnquiryRequest();
        enquiryRequest.setAccountNumber(accountNumber);
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/user/NameEnquiry")
    public String userNameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/user/CreditIn")
    public BankResponse creditIn(@RequestBody CreditDebitRequest crRequest) {
        return userService.creditAccount(crRequest);
    }

    @PostMapping("/user/DebitOut")
    public BankResponse debitOut(@RequestBody CreditDebitRequest drRequest) {
        return userService.debitAccount(drRequest);
    }

    @PostMapping("/user/Transfer")
    public BankResponse transferAmount(@RequestBody Transfer transferRequest) {
		return userService.transfer(transferRequest);
	}
	
    @GetMapping("/getIFSC")
    public Banks getIfsc(String accountNumber) {
            return bankingService.getIfsc(accountNumber);
    }
}
