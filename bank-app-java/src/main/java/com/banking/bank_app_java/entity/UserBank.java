package com.banking.bank_app_java.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "User_Bank")
public class UserBank {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to User
    private User user;
	
	@ManyToOne
    @JoinColumn(name = "ifsc") // foreign key column in Bank table
    private Banks bank;

	private String accNo;
	private BigDecimal accBalance;
	private String accStatus;
}
