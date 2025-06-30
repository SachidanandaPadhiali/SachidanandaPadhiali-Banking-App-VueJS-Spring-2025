package com.banking.bank_app_java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banking.bank_app_java.entity.Banks;
import com.banking.bank_app_java.entity.User;
import com.banking.bank_app_java.entity.UserBank;

public interface UserBankRepo extends JpaRepository<UserBank, Long> {
	boolean existsByUserAndBank(User user, Banks bank);

	UserBank findByAccNo(String accNo);
	Boolean existsByAccNo(String accNo);

	@Query("SELECT u.bank.ifsc FROM UserBank u WHERE u.accNo = :accountNumber")
	String searchIFSCById(@Param("accountNumber") String accountNumber);

}
