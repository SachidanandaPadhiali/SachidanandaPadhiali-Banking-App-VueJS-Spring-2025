package com.banking.bank_app_java.controller;

import com.banking.bank_app_java.dto.*;
import com.banking.bank_app_java.entity.User;
import com.banking.bank_app_java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8088")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequests userRequests) {
        User user = userService.validateUser(userRequests.getEmail(), userRequests.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/user")
    public BankResponse createAccount(@RequestBody UserRequests userRequests) {
        return userService.createAccount(userRequests);
    }

    @GetMapping("/user/BalanceEnquiry")
    public BankResponse accountBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
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

}
