package com.bank.msaccount.dto;

import com.bank.msaccount.enums.AccountType;
import com.bank.msaccount.enums.MovementType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Data transfer object for report responses
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime movementDate;

    private String name;

    private String accountNumber;

    private AccountType accountType;

    private BigDecimal previousBalance;

    private Boolean status;

    private BigDecimal amount;

    private MovementType movementType;

    private BigDecimal currentBalance;
}
