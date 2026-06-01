package com.bank.msaccount.service;

import com.bank.msaccount.dto.MovementDetailResponseDto;
import com.bank.msaccount.dto.MovementRequestDto;
import com.bank.msaccount.dto.MovementResponseDto;
import com.bank.msaccount.enums.MovementType;
import com.bank.msaccount.exception.AccountInactiveException;
import com.bank.msaccount.exception.AccountNotFoundException;
import com.bank.msaccount.exception.InsufficientBalanceException;
import com.bank.msaccount.exception.InvalidAmountException;
import com.bank.msaccount.exception.MovementNotFoundException;
import com.bank.msaccount.exception.SameAccountException;
import com.bank.msaccount.model.Account;
import com.bank.msaccount.model.Movement;
import com.bank.msaccount.repository.AccountRepository;
import com.bank.msaccount.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementation of the MovementService interface
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl implements MovementService {

    private final AccountRepository accountRepository;

    private final MovementRepository movementRepository;

    @Override
    @Transactional
    public Mono<MovementResponseDto> save(MovementRequestDto movementRequest) {
        log.info("Start save movement. amount = {}, sourceAccountId = {}, destinationAccountId = {}",
                movementRequest.getAmount(),
                movementRequest.getSourceAccountId(),
                movementRequest.getDestinationAccountId()
        );
        return Mono.zip(
                        accountRepository.findById(movementRequest.getSourceAccountId())
                                .switchIfEmpty(
                                        Mono.error(new AccountNotFoundException(
                                                movementRequest.getSourceAccountId()))),
                        accountRepository.findById(movementRequest.getDestinationAccountId())
                                .switchIfEmpty(
                                        Mono.error(new AccountNotFoundException(
                                                movementRequest.getDestinationAccountId())))
                )
                .flatMap(tuple -> {

                    Account source = tuple.getT1();
                    Account destination = tuple.getT2();

                    validateMovement(source, destination, movementRequest.getAmount());

                    BigDecimal sourcePreviousBalance = source.getBalance();
                    BigDecimal destinationPreviousBalance = destination.getBalance();

                    source.setBalance(source.getBalance().subtract(movementRequest.getAmount()));
                    destination.setBalance(destination.getBalance().add(movementRequest.getAmount()));

                    Movement debitMovement = Movement.builder()
                            .movementDate(LocalDateTime.now())
                            .type(MovementType.DEBIT)
                            .amount(movementRequest.getAmount())
                            .previousBalance(sourcePreviousBalance)
                            .currentBalance(source.getBalance())
                            .status(true)
                            .accountId(source.getId())
                            .build();

                    Movement creditMovement = Movement.builder()
                            .movementDate(LocalDateTime.now())
                            .type(MovementType.CREDIT)
                            .amount(movementRequest.getAmount())
                            .previousBalance(destinationPreviousBalance)
                            .currentBalance(destination.getBalance())
                            .status(true)
                            .accountId(destination.getId())
                            .build();

                    return accountRepository.save(source)
                            .then(accountRepository.save(destination))
                            .then(movementRepository.save(debitMovement))
                            .then(movementRepository.save(creditMovement))
                            .thenReturn(
                                    MovementResponseDto.builder()
                                            .message("Transfer completed successfully")
                                            .amount(movementRequest.getAmount())
                                            .sourceAccountId(source.getId())
                                            .destinationAccountId(destination.getId())
                                            .build());
                })
                .doOnSuccess(response ->
                        log.info("End save movement. amount = {}, sourceAccountId = {}, destinationAccountId = {}",
                                movementRequest.getAmount(),
                                movementRequest.getSourceAccountId(),
                                movementRequest.getDestinationAccountId()
                        ));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovementDetailResponseDto> findAll() {
        return movementRepository.findAll()
                .map(movement -> MovementDetailResponseDto.builder()
                        .id(movement.getId())
                        .movementDate(movement.getMovementDate())
                        .type(movement.getType())
                        .amount(movement.getAmount())
                        .balance(movement.getCurrentBalance())
                        .status(movement.getStatus())
                        .accountId(movement.getAccountId())
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MovementDetailResponseDto> findById(Long id) {
        return movementRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovementNotFoundException(id)))
                .map(movement -> MovementDetailResponseDto.builder()
                        .id(movement.getId())
                        .movementDate(movement.getMovementDate())
                        .type(movement.getType())
                        .amount(movement.getAmount())
                        .balance(movement.getCurrentBalance())
                        .status(movement.getStatus())
                        .accountId(movement.getAccountId())
                        .build());
    }

    private void validateMovement(Account source, Account destination, BigDecimal amount) {
        if (!Boolean.TRUE.equals(source.getStatus())) {
            throw new AccountInactiveException(source.getId());
        }

        if (!Boolean.TRUE.equals(destination.getStatus())) {
            throw new AccountInactiveException(destination.getId());
        }

        if (source.getId().equals(destination.getId())) {
            throw new SameAccountException("Source and destination accounts cannot be the same");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("The amount must be greater than zero");
        }

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Balance not available");
        }
    }
}
