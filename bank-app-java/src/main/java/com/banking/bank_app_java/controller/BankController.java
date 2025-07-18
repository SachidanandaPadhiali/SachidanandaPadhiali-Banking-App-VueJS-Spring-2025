package com.banking.bank_app_java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.bank_app_java.dto.BankResponse;
import com.banking.bank_app_java.service.BankingService;
import com.banking.bank_app_java.dto.BanksDTO;

@CrossOrigin(
        origins = "http://192.168.1.4.nip.io:8080",
        allowedHeaders = "*",
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/bank")
public class BankController {

	@Autowired
	BankingService bankingService;
	
	@PostMapping("/new")
    public BankResponse createAccount(@RequestBody BanksDTO bank) {
        return bankingService.newBank(bank.getAddress());
    }

	@GetMapping("/addresses")
	public ResponseEntity<List<String>> getAddresses() {
		return ResponseEntity.ok(bankingService.getAllAddresses());
	}
	
}
