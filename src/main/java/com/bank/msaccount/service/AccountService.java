package com.bank.msaccount.service;

import com.bank.msaccount.dto.AccountRequestDto;
import com.bank.msaccount.dto.AccountResponseDto;
import com.bank.msaccount.dto.UpdateAccountRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for account operations
 */
public interface AccountService {

    /**
     * Saves a new account
     *
     * @param accountRequest the account request {@link AccountRequestDto}
     * @return a Mono containing the saved account {@link AccountResponseDto}
     */
    Mono<AccountResponseDto> save(AccountRequestDto accountRequest);

    /**
     * Finds all accounts
     *
     * @return a Flux containing all accounts {@link AccountResponseDto}
     */
    Flux<AccountResponseDto> findAll();

    /**
     * Finds an account by ID
     *
     * @param id the ID of the account to retrieve
     * @return a Mono containing the retrieved account {@link AccountResponseDto}
     */
    Mono<AccountResponseDto> findById(Long id);

    /**
     * Finds an account by number
     *
     * @param number the number of the account to retrieve
     * @return a Mono containing the retrieved account {@link AccountResponseDto}
     */
    Mono<AccountResponseDto> findByNumber(String number);

    /**
     * Updates a account by ID
     *
     * @param id             the ID of the account
     * @param accountRequest the account request {@link UpdateAccountRequestDto}
     * @return a Mono containing the updated account {@link AccountResponseDto}
     */
    Mono<AccountResponseDto> update(Long id, UpdateAccountRequestDto accountRequest);

    /**
     * Deletes an account by ID
     *
     * @param id the ID of the account to delete
     * @return a Mono of the no content response
     */
    Mono<Void> delete(Long id);
}
