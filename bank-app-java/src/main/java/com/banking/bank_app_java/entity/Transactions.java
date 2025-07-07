package com.banking.bank_app_java.entity;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User_Transaction")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_type", nullable = false)
    private int transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_acc_no", referencedColumnName = "acc_no")
    private UserBank sourceAcc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dest_acc_no", referencedColumnName = "acc_no")
    private UserBank destAcc;

    @Column(name = "transaction_amt", nullable = false)
    private BigDecimal transactionAmt;

    @Column(name = "transaction_status", nullable = false)
    private String transactionStatus;

    @CreationTimestamp
    @Column(name = "transaction_time", nullable = false, updatable = false)
    private LocalDateTime transactionTime;

}
