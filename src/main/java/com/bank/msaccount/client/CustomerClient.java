package com.bank.msaccount.client;

import com.bank.msaccount.dto.CustomerResponseDto;
import reactor.core.publisher.Mono;

/**
 * Client interface for interacting with the customer service
 */
public interface CustomerClient {

    /**
     * Finds a customer by ID
     *
     * @param customerId the ID of the customer to find
     * @return a Mono containing the customer response {@link CustomerResponseDto}
     */
    Mono<CustomerResponseDto> findCustomerById(Long customerId);
}
