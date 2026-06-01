package com.bank.msaccount.service;

import com.bank.msaccount.client.CustomerClient;
import com.bank.msaccount.dto.AccountRequestDto;
import com.bank.msaccount.dto.AccountResponseDto;
import com.bank.msaccount.dto.CustomerResponseDto;
import com.bank.msaccount.dto.UpdateAccountRequestDto;
import com.bank.msaccount.exception.AccountNotFoundException;
import com.bank.msaccount.exception.CustomerInactiveException;
import com.bank.msaccount.model.Account;
import com.bank.msaccount.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Implementation of the {@link AccountService} interface
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final CustomerClient customerClient;

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Mono<AccountResponseDto> save(AccountRequestDto accountRequest) {
        log.info("Start save account");

        Account account = Account.builder()
                .number(accountRequest.getNumber())
                .type(accountRequest.getType())
                .initialBalance(accountRequest.getInitialBalance())
                .balance(accountRequest.getInitialBalance())
                .status(true)
                .customerId(accountRequest.getCustomerId())
                .createdAt(LocalDateTime.now())
                .build();

        return customerClient.findCustomerById(account.getCustomerId())
                .filter(CustomerResponseDto::getStatus)
                .switchIfEmpty(Mono.error(
                        new CustomerInactiveException(account.getCustomerId())))
                .flatMap(response -> {
                    account.setCreatedAt(LocalDateTime.now());
                    return accountRepository.save(account)
                            .map(accountSaved -> AccountResponseDto.builder()
                                    .id(accountSaved.getId())
                                    .number(accountSaved.getNumber())
                                    .type(accountSaved.getType())
                                    .initialBalance(accountSaved.getInitialBalance())
                                    .status(accountSaved.getStatus())
                                    .createdAt(accountSaved.getCreatedAt())
                                    .customerId(accountSaved.getCustomerId())
                                    .build());
                })
                .doOnSuccess(response ->
                        log.info("End save account. id = {}, type = {}, status = {}, customerId = {}",
                                Objects.requireNonNull(response).getId(),
                                response.getType(),
                                response.getStatus(),
                                response.getCustomerId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AccountResponseDto> findAll() {
        return accountRepository.findAll()
                .map(accountSaved -> AccountResponseDto.builder()
                        .id(accountSaved.getId())
                        .number(accountSaved.getNumber())
                        .type(accountSaved.getType())
                        .balance(accountSaved.getBalance())
                        .status(accountSaved.getStatus())
                        .customerId(accountSaved.getCustomerId())
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AccountResponseDto> findById(Long id) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new AccountNotFoundException(id)))
                .map(accountSaved -> AccountResponseDto.builder()
                        .id(accountSaved.getId())
                        .number(accountSaved.getNumber())
                        .type(accountSaved.getType())
                        .balance(accountSaved.getBalance())
                        .status(accountSaved.getStatus())
                        .customerId(accountSaved.getCustomerId())
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AccountResponseDto> findByNumber(String number) {
        return accountRepository.findByNumber(number)
                .switchIfEmpty(Mono.error(new AccountNotFoundException(number)))
                .map(accountSaved -> AccountResponseDto.builder()
                        .id(accountSaved.getId())
                        .number(accountSaved.getNumber())
                        .type(accountSaved.getType())
                        .balance(accountSaved.getBalance())
                        .status(accountSaved.getStatus())
                        .customerId(accountSaved.getCustomerId())
                        .build());
    }

    @Override
    public Mono<AccountResponseDto> update(Long id, UpdateAccountRequestDto accountRequest) {
        log.info("Start update account");
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new AccountNotFoundException(id)))
                .flatMap(account -> {
                    account.setStatus(accountRequest.getStatus());
                    account.setModifiedAt(LocalDateTime.now());
                    return accountRepository.save(account)
                            .map(accountUpdated -> AccountResponseDto.builder()
                                    .id(accountUpdated.getId())
                                    .number(accountUpdated.getNumber())
                                    .type(accountUpdated.getType())
                                    .initialBalance(accountUpdated.getInitialBalance())
                                    .status(accountUpdated.getStatus())
                                    .customerId(accountUpdated.getCustomerId())
                                    .modifiedAt(accountUpdated.getModifiedAt())
                                    .build());
                })
                .doOnSuccess(response ->
                        log.info("End update account. id = {}, type = {}, status = {}, customerId = {}",
                                Objects.requireNonNull(response).getId(),
                                response.getType(),
                                response.getStatus(),
                                response.getCustomerId()));
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.info("Start delete account");
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new AccountNotFoundException(id)))
                .flatMap(accountRepository::delete)
                .doOnSuccess(response ->
                        log.info("End delete account. id = {}", id));
    }
}
