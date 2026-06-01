package com.bank.msaccount.dto;

import com.bank.msaccount.enums.MovementType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request data transfer object for a simple movement
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementSimpleRequestDto {

    @NotNull(message = "It must not be null")
    @DecimalMin(value = "1.00", message = "The amount cannot be less than 1")
    private BigDecimal amount;

    @NotNull(message = "It must not be null")
    private String accountNumber;

    @NotNull(message = "It must not be null")
    private MovementType movementType;
}
