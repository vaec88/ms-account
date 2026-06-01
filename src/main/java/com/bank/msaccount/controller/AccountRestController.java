package com.bank.msaccount.controller;

import com.bank.msaccount.dto.AccountRequestDto;
import com.bank.msaccount.dto.AccountResponseDto;
import com.bank.msaccount.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Rest controller for accounts operations
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    /**
     * Saves a new account
     *
     * @param accountRequest the account request {@link AccountRequestDto}
     * @return a Mono containing the saved account {@link AccountResponseDto}
     */
    @PostMapping
    public Mono<ResponseEntity<AccountResponseDto>> save(@Valid @RequestBody AccountRequestDto accountRequest) {
        return accountService.save(accountRequest)
                .map(response -> ResponseEntity
                        .created(URI.create("/api/v1/accounts" + response.getId()))
                        .body(response)
                );
    }

    /**
     * Retrieves all accounts
     *
     * @return a Flux containing all accounts {@link AccountResponseDto}
     */
    @GetMapping
    public Flux<AccountResponseDto> findAll() {
        return accountService.findAll();
    }

    /**
     * Retrieves an account by ID
     *
     * @param id the ID of the account to retrieve
     * @return a Mono containing the retrieved account {@link AccountResponseDto}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountResponseDto>> findById(@PathVariable Long id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok);
    }

    /**
     * Updates a account by ID
     *
     * @param id             the ID of the account
     * @param accountRequest the account request {@link AccountRequestDto}
     * @return a Mono containing the updated account {@link AccountResponseDto}
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequestDto accountRequest) {
        return accountService.update(id, accountRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Deletes an account by ID
     *
     * @param id the ID of the account to delete
     * @return a Mono of the no content response
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return accountService.delete(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

}
