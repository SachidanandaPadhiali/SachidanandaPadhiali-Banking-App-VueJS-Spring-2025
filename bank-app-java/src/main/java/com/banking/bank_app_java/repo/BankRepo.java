package com.banking.bank_app_java.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.banking.bank_app_java.entity.Banks;

public interface BankRepo extends JpaRepository<Banks, String> {
	Banks findByAddress(String address);
	Banks findByIfsc(String ifsc);
	
	@Query("SELECT DISTINCT b.address FROM Banks b")
    List<String> findAllDistinctAddresses();
}
