package com.bank.msaccount.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents an error response
 */
@Data
@Builder
public class ErrorResponseDto {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;
}
