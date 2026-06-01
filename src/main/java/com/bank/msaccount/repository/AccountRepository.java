package com.bank.msaccount.repository;

import com.bank.msaccount.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repository interface for account operations
 */
@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

    /**
     * Find an account by number
     *
     * @param number the account number
     * @return the account
     */
    Mono<Account> findByNumber(String number);
}
