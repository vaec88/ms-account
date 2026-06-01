package com.bank.msaccount.client;

import com.bank.msaccount.dto.CustomerResponseDto;
import com.bank.msaccount.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerClientImpl implements CustomerClient {

    private final WebClient webClient;

    @Override
    public Mono<CustomerResponseDto> findCustomerById(Long customerId) {
        return webClient.get()
                .uri("/api/v1/customers/{id}", customerId)
                .retrieve()
                .onStatus(
                        status -> 404 == status.value(),
                        response -> Mono.error(
                                new CustomerNotFoundException(customerId)
                        )
                )
                .bodyToMono(CustomerResponseDto.class);
    }
}
