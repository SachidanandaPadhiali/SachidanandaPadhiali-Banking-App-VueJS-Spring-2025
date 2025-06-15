package com.banking.bank_app_java.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Banks")
public class Banks {

	@Id
	private String ifsc;
	private String address;
	
	@OneToMany(mappedBy = "bank")
    private List<UserBank> userMappings;
}
