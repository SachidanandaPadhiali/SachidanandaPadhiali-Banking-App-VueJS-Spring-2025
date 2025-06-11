package com.banking.bank_app_java.dto;

import com.banking.bank_app_java.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetails {
    private String recipient;
    private String subject;
    private String msgBody;
    private String attachment;
    private User savedUser;
}
