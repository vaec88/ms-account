package com.bank.msaccount.repository;

import com.bank.msaccount.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for account operations
 */
@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {
}
