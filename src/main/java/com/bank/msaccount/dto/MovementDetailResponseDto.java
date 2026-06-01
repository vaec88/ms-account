package com.bank.msaccount.dto;

import com.bank.msaccount.enums.MovementType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Data transfer object for movement details
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementDetailResponseDto {

    private Long id;

    private LocalDateTime movementDate;

    private MovementType type;

    private BigDecimal amount;

    private BigDecimal balance;

    private Boolean status;

    private Long accountId;
}
