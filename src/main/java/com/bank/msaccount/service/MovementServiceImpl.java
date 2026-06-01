package com.bank.msaccount.service;

import com.bank.msaccount.dto.MovementDetailResponseDto;
import com.bank.msaccount.dto.MovementRequestDto;
import com.bank.msaccount.dto.MovementResponseDto;
import com.bank.msaccount.dto.MovementSimpleRequestDto;
import com.bank.msaccount.dto.MovementSimpleResponseDto;
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
import java.util.Objects;

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
    @Transactional
    public Mono<MovementSimpleResponseDto> saveSimple(MovementSimpleRequestDto movementRequest) {
        log.info("Start save simple movement. amount = {}, accountNumber = {}, movementType = {}",
                movementRequest.getAmount(),
                movementRequest.getAccountNumber(),
                movementRequest.getMovementType()
        );
        return
                accountRepository.findByNumber(movementRequest.getAccountNumber())
                        .switchIfEmpty(
                                Mono.error(new AccountNotFoundException(
                                        movementRequest.getAccountNumber())))
                        .flatMap(accountFound -> {

                            validateMovement(accountFound, movementRequest);

                            BigDecimal previousBalance = accountFound.getBalance();

                            BigDecimal currentBalance = calculateBalance(
                                    previousBalance,
                                    movementRequest.getAmount(),
                                    movementRequest.getMovementType());
                            accountFound.setBalance(currentBalance);

                            Movement simpleMovement = Movement.builder()
                                    .movementDate(LocalDateTime.now())
                                    .type(movementRequest.getMovementType())
                                    .amount(movementRequest.getAmount())
                                    .previousBalance(previousBalance)
                                    .currentBalance(accountFound.getBalance())
                                    .status(true)
                                    .accountId(accountFound.getId())
                                    .build();

                            return accountRepository.save(accountFound)
                                    .then(movementRepository.save(simpleMovement));
                        })
                        .map(simpleMovement -> MovementSimpleResponseDto.builder()
                                .movementDate(simpleMovement.getMovementDate())
                                .type(simpleMovement.getType())
                                .amount(simpleMovement.getAmount())
                                .previousBalance(simpleMovement.getPreviousBalance())
                                .currentBalance(simpleMovement.getCurrentBalance())
                                .status(simpleMovement.getStatus())
                                .message("Movement completed successfully")
                                .build())
                        .doOnSuccess(response ->
                                log.info("End save simple movement. amount = {}, accountNumber = {}, movementType = {}, currentBalance = {}",
                                        movementRequest.getAmount(),
                                        movementRequest.getAccountNumber(),
                                        movementRequest.getMovementType(),
                                        Objects.requireNonNull(response).getCurrentBalance()
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

    /**
     * Validates the transfer movement
     *
     * @param source      source account
     * @param destination destination account
     * @param amount      amount to transfer
     */
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

    /**
     * Validates the simple movement
     *
     * @param account the account
     * @param request the request with the movement details
     */
    private void validateMovement(Account account, MovementSimpleRequestDto request) {
        if (!Boolean.TRUE.equals(account.getStatus())) {
            throw new AccountInactiveException(account.getNumber());
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("The amount must be greater than zero");
        }

        if (MovementType.DEBIT.equals(request.getMovementType())
                && account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Balance not available");
        }
    }

    /**
     * Calculates the new balance based on the movement type
     *
     * @param currentBalance the account current balance
     * @param amount         the amount
     * @param type           the movement type
     * @return the new balance
     */
    private BigDecimal calculateBalance(BigDecimal currentBalance, BigDecimal amount, MovementType type) {
        return MovementType.DEBIT.equals(type)
                ? currentBalance.subtract(amount)
                : currentBalance.add(amount);
    }
}
