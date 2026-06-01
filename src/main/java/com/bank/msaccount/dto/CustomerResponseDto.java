package com.bank.msaccount.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a Data transfer object for customer responses
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponseDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String identification;

    private String address;

    private String phone;

    private Boolean status;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
