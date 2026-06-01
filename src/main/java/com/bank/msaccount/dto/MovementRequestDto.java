package com.bank.msaccount.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a Data transfer object for movement requests
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementRequestDto {

    @NotNull(message = "It must not be null")
    @DecimalMin(value = "1.00", message = "The amount cannot be less than 1")
    private BigDecimal amount;

    @NotNull(message = "It must not be null")
    private Long sourceAccountId;

    @NotNull(message = "It must not be null")
    private Long destinationAccountId;

}
