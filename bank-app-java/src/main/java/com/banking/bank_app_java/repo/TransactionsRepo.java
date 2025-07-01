package com.banking.bank_app_java.repo;

import com.banking.bank_app_java.dto.Transactions;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionsRepo extends JpaRepository<Transactions, BigDecimal> {

@Query("SELECT t.* FROM Transaction t where t.")
    List<String> findAllDistinctAddresses();    
}
