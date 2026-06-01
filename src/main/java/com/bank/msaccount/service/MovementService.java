package com.bank.msaccount.service;

import com.bank.msaccount.dto.MovementDetailResponseDto;
import com.bank.msaccount.dto.MovementRequestDto;
import com.bank.msaccount.dto.MovementResponseDto;
import com.bank.msaccount.dto.MovementSimpleRequestDto;
import com.bank.msaccount.dto.MovementSimpleResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface for movement service
 */
public interface MovementService {

    /**
     * Save a movement
     *
     * @param movementRequest the movement request {@link MovementRequestDto}
     * @return a Mono containing the saved movement {@link MovementResponseDto}
     */
    Mono<MovementResponseDto> save(MovementRequestDto movementRequest);

    /**
     * Save a simple movement
     *
     * @param movementRequest the movement request {@link MovementSimpleRequestDto}
     * @return a Mono containing the saved movement {@link MovementSimpleResponseDto}
     */
    Mono<MovementSimpleResponseDto> saveSimple(MovementSimpleRequestDto movementRequest);

    /**
     * Finds all movements
     *
     * @return a Flux containing all movements {@link MovementDetailResponseDto}
     */
    Flux<MovementDetailResponseDto> findAll();

    /**
     * Finds a movement by ID
     *
     * @param id the ID of the movement to retrieve
     * @return a Mono containing the retrieved movement {@link MovementDetailResponseDto}
     */
    Mono<MovementDetailResponseDto> findById(Long id);
}
