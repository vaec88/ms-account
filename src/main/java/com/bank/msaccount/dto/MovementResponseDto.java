package com.bank.msaccount.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a Data transfer object for movement responses
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementResponseDto {

    private String message;

    private BigDecimal amount;

    private Long sourceAccountId;

    private Long destinationAccountId;
}
