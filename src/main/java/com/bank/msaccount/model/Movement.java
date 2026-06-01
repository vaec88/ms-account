package com.bank.msaccount.model;

import com.bank.msaccount.enums.MovementType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a movement entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("movement")
public class Movement {

    @Id
    private Long id;

    @Column("movement_date")
    @NotNull
    private LocalDateTime movementDate;

    private MovementType type;

    @NotNull
    private BigDecimal amount;

    @Column("previous_balance")
    private BigDecimal previousBalance;

    @Column("current_balance")
    @NotNull
    private BigDecimal currentBalance;

    private Boolean status;

    @Column("account_id")
    @NotNull
    private Long accountId;
}
