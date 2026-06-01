package com.bank.msaccount.dto;

import com.bank.msaccount.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Data transfer object for account responses
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponseDto {

    private Long id;

    private String number;

    private AccountType type;

    private BigDecimal initialBalance;

    private BigDecimal balance;

    private Boolean status;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Long customerId;

}
