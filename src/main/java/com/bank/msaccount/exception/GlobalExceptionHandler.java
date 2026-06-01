package com.bank.msaccount.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Represents a global exception handler
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles not found and inactive exceptions
     *
     * @param ex       {@link RuntimeException}
     * @param exchange {@link ServerWebExchange}
     * @return a Mono of {@link ResponseEntity<ErrorResponseDto>} with error details
     */
    @ExceptionHandler({
            CustomerNotFoundException.class,
            CustomerInactiveException.class,
            AccountNotFoundException.class,
            AccountInactiveException.class,
            MovementNotFoundException.class
    })
    public Mono<ResponseEntity<ErrorResponseDto>> handleNotFound(
            RuntimeException ex,
            ServerWebExchange exchange) {

        ErrorResponseDto response = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();

        log.warn(ex.getMessage());

        return Mono.just(
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(response)
        );
    }

    /**
     * Handles bad request exception
     *
     * @param ex       {@link RuntimeException}
     * @param exchange {@link ServerWebExchange}
     * @return a Mono of {@link ResponseEntity<ErrorResponseDto>} with error details
     */
    @ExceptionHandler({
            InvalidAmountException.class,
            InvalidDateRangeException.class,
            InvalidDateFormatException.class
    })
    public Mono<ResponseEntity<ErrorResponseDto>> handleBadRequest(
            RuntimeException ex,
            ServerWebExchange exchange) {

        ErrorResponseDto response = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();

        log.warn(ex.getMessage());

        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response)
        );
    }

    /**
     * Handles validation exception
     *
     * @param ex       {@link WebExchangeBindException}
     * @param exchange {@link ServerWebExchange}
     * @return a Mono of {@link ResponseEntity<ErrorResponseDto>} with error details
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleValidationException(
            WebExchangeBindException ex,
            ServerWebExchange exchange) {

        String message = ex.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ErrorResponseDto response = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(exchange.getRequest().getPath().value())
                .build();

        log.warn(ex.getMessage());

        return Mono.just(
                ResponseEntity.badRequest()
                        .body(response)
        );
    }

    /**
     * Handles general exception
     *
     * @param ex       {@link Exception}
     * @param exchange {@link ServerWebExchange}
     * @return a Mono of {@link ResponseEntity<ErrorResponseDto>} with error details
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleGenericException(
            Exception ex,
            ServerWebExchange exchange) {

        ErrorResponseDto response = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();

        log.error(ex.getMessage());

        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response)
        );
    }
}
