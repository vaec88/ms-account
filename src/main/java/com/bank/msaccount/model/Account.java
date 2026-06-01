package com.bank.msaccount.model;

import com.bank.msaccount.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
 * Represents an account entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("account")
public class Account {

    @Id
    private Long id;

    @NotBlank
    private String number;

    @NotNull
    private AccountType type;

    @Column("initial_balance")
    @NotNull
    @DecimalMin(value = "0.00", message = "The initial balance cannot be less than 0")
    private BigDecimal initialBalance;

    @NotNull
    @DecimalMin(value = "0.00", message = "The balance cannot be less than 0")
    private BigDecimal balance;

    private Boolean status;

    @Column("created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column("modified_at")
    private LocalDateTime modifiedAt;

    @Column("customer_id")
    @NotNull
    private Long customerId;

}
