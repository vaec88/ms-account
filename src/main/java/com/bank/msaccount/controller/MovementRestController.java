package com.bank.msaccount.controller;

import com.bank.msaccount.dto.MovementDetailResponseDto;
import com.bank.msaccount.dto.MovementRequestDto;
import com.bank.msaccount.dto.MovementResponseDto;
import com.bank.msaccount.dto.MovementSimpleRequestDto;
import com.bank.msaccount.dto.MovementSimpleResponseDto;
import com.bank.msaccount.service.MovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Rest controller for movements operations
 */
@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementRestController {

    private final MovementService movementService;

    /**
     * Saves a new movement
     *
     * @param movementRequest the movement request {@link MovementRequestDto}
     * @return a Mono containing the saved movement {@link MovementResponseDto}
     */
    @PostMapping
    public Mono<MovementResponseDto> save(@Valid @RequestBody MovementRequestDto movementRequest) {
        return movementService.save(movementRequest);
    }

    @PostMapping("/simple")
    public Mono<MovementSimpleResponseDto> saveSimple(@Valid @RequestBody MovementSimpleRequestDto movementRequest) {
        return movementService.saveSimple(movementRequest);
    }

    /**
     * Retrieves all movements
     *
     * @return a Flux containing all movements {@link MovementDetailResponseDto}
     */
    @GetMapping
    public Flux<MovementDetailResponseDto> findAll() {
        return movementService.findAll();
    }

    /**
     * Retrieves a movement by ID
     *
     * @param id the ID of the movement to retrieve
     * @return a Mono containing the retrieved movement {@link MovementDetailResponseDto}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovementDetailResponseDto>> findById(@PathVariable Long id) {
        return movementService.findById(id)
                .map(ResponseEntity::ok);
    }
}
