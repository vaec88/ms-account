package com.bank.msaccount.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Data transfer object for report queries
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportQueryDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime movementDate;

    private String accountNumber;

    private String accountType;

    private BigDecimal previousBalance;

    private Boolean status;

    private BigDecimal amount;

    private String movementType;

    private BigDecimal currentBalance;
}
