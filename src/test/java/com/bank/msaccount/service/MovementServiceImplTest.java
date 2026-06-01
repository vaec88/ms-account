package com.bank.msaccount.service;

import com.bank.msaccount.dto.MovementRequestDto;
import com.bank.msaccount.exception.AccountInactiveException;
import com.bank.msaccount.exception.AccountNotFoundException;
import com.bank.msaccount.exception.InsufficientBalanceException;
import com.bank.msaccount.exception.InvalidAmountException;
import com.bank.msaccount.exception.MovementNotFoundException;
import com.bank.msaccount.exception.SameAccountException;
import com.bank.msaccount.model.Account;
import com.bank.msaccount.model.Movement;
import com.bank.msaccount.repository.AccountRepository;
import com.bank.msaccount.repository.MovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MovementRepository movementRepository;

    @InjectMocks
    private MovementServiceImpl service;

    private Account sourceAccount;

    private Account destinationAccount;

    private MovementRequestDto request;

    @BeforeEach
    void setUp() {

        sourceAccount = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(1000))
                .status(true)
                .build();

        destinationAccount = Account.builder()
                .id(2L)
                .balance(BigDecimal.valueOf(500))
                .status(true)
                .build();

        request = MovementRequestDto.builder()
                .amount(BigDecimal.valueOf(100))
                .sourceAccountId(1L)
                .destinationAccountId(2L)
                .build();
    }

    @Test
    void shouldThrowWhenSourceAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Mono.empty());
        when(accountRepository.findById(2L)).thenReturn(Mono.just(destinationAccount));

        StepVerifier.create(service.save(request))
                .expectError(AccountNotFoundException.class)
                .verify();

        verify(accountRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDestinationAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Mono.just(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Mono.empty());

        StepVerifier.create(service.save(request))
                .expectError(AccountNotFoundException.class)
                .verify();

        verify(accountRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenSourceAccountInactive() {
        sourceAccount.setStatus(false);
        when(accountRepository.findById(1L)).thenReturn(Mono.just(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Mono.just(destinationAccount));

        StepVerifier.create(service.save(request))
                .expectError(AccountInactiveException.class)
                .verify();

        verify(accountRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDestinationAccountInactive() {
        destinationAccount.setStatus(false);
        when(accountRepository.findById(1L)).thenReturn(Mono.just(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Mono.just(destinationAccount));

        StepVerifier.create(service.save(request))
                .expectError(AccountInactiveException.class)
                .verify();

        verify(accountRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAccountsAreTheSame() {
        request.setDestinationAccountId(1L);
        when(accountRepository.findById(1L)).thenReturn(Mono.just(sourceAccount));

        StepVerifier.create(service.save(request))
                .expectError(SameAccountException.class)
                .verify();

        verify(accountRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAmountIsZero() {
        request.setAmount(BigDecimal.ZERO);
        when(accountRepository.findById(1L)).thenReturn(Mono.just(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Mono.just(destinationAccount));

        StepVerifier.create(service.save(request))
                .expectError(InvalidAmountException.class)
                .verify();

        verify(accountRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAmountIsNegative() {
        request.setAmount(BigDecimal.valueOf(-10));
        when(accountRepository.findById(1L)).thenReturn(Mono.just(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Mono.just(destinationAccount));

        StepVerifier.create(service.save(request))
                .expectError(InvalidAmountException.class)
                .verify();

        verify(accountRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenBalanceIsInsufficient() {
        sourceAccount.setBalance(BigDecimal.valueOf(50));
        when(accountRepository.findById(1L)).thenReturn(Mono.just(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Mono.just(destinationAccount));

        StepVerifier.create(service.save(request))
                .expectError(InsufficientBalanceException.class)
                .verify();

        verify(accountRepository, never()).save(any());
        verify(movementRepository, never()).save(any());
    }

    @Test
    void shouldTransferSuccessfully() {
        when(accountRepository.findById(1L)).thenReturn(Mono.just(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Mono.just(destinationAccount));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation ->
                        Mono.just(invocation.getArgument(0)));

        when(movementRepository.save(any(Movement.class)))
                .thenAnswer(invocation ->
                        Mono.just(invocation.getArgument(0)));

        StepVerifier.create(service.save(request))
                .assertNext(response -> {
                    assertEquals(BigDecimal.valueOf(100), response.getAmount());
                    assertEquals(1L, response.getSourceAccountId());
                    assertEquals(2L, response.getDestinationAccountId());
                    assertEquals("Transfer completed successfully", response.getMessage());
                })
                .verifyComplete();

        assertEquals(BigDecimal.valueOf(900), sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(600), destinationAccount.getBalance());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(movementRepository, times(2)).save(any(Movement.class));
    }

    @Test
    void shouldFindAllMovements() {
        Movement movement = Movement.builder()
                .id(1L)
                .amount(BigDecimal.TEN)
                .accountId(1L)
                .status(true)
                .build();

        when(movementRepository.findAll()).thenReturn(Flux.just(movement));

        StepVerifier.create(service.findAll())
                .assertNext(response -> {
                    assertEquals(1L, response.getId());
                    assertEquals(BigDecimal.TEN, response.getAmount());
                })
                .verifyComplete();
    }

    @Test
    void shouldThrowWhenMovementNotFound() {
        when(movementRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.findById(1L))
                .expectError(MovementNotFoundException.class)
                .verify();
    }

    @Test
    void shouldFindMovementById() {
        Movement movement = Movement.builder()
                .id(1L)
                .amount(BigDecimal.TEN)
                .accountId(1L)
                .status(true)
                .build();

        when(movementRepository.findById(1L)).thenReturn(Mono.just(movement));

        StepVerifier.create(service.findById(1L))
                .assertNext(response ->
                        assertEquals(1L, response.getId()))
                .verifyComplete();
    }

}