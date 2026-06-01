package com.bank.msaccount.dto;

import com.bank.msaccount.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a Data transfer object for account requests
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {

    @NotBlank(message = "It must not be empty")
    private String number;

    @NotNull(message = "It must not be null")
    private AccountType type;

    @NotNull(message = "It must not be null")
    @DecimalMin(value = "0.00", message = "The initial balance cannot be less than 0")
    private BigDecimal initialBalance;

    private Boolean status;

    @NotNull(message = "It must not be null")
    private Long customerId;
}
