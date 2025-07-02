package com.banking.bank_app_java.repo;

import com.banking.bank_app_java.dto.TransactionsDTO;
import com.banking.bank_app_java.entity.Transactions;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepo extends JpaRepository<Transactions, BigDecimal> {

    List<TransactionsDTO> findBySourceAcc_AccountNumberOrDestAcc_AccountNumberOrderByTransactionTimeDesc(
            String sourceAccountNumber,
            String destAccountNumber
    );

}
