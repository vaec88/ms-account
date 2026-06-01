package com.bank.msaccount.repository;

import com.bank.msaccount.model.Movement;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for movement operations
 */
@Repository
public interface MovementRepository extends ReactiveCrudRepository<Movement, Long> {
}
