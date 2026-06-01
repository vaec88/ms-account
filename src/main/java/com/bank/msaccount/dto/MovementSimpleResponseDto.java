package com.bank.msaccount.dto;

import com.bank.msaccount.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response data transfer object for a simple movement
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementSimpleResponseDto {

    private String accountNumber;

    private LocalDateTime movementDate;

    private MovementType type;

    private BigDecimal amount;

    private BigDecimal previousBalance;

    private BigDecimal currentBalance;

    private Boolean status;

    private String message;

}
