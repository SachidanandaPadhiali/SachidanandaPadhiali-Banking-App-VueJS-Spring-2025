package com.banking.bank_app_java.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.bank_app_java.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Boolean existsByEmail(String email);
	User findByEmail(String email);
}
