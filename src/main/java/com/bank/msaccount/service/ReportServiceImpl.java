package com.bank.msaccount.service;

import com.bank.msaccount.client.CustomerClient;
import com.bank.msaccount.dto.ReportResponseDto;
import com.bank.msaccount.enums.AccountType;
import com.bank.msaccount.enums.MovementType;
import com.bank.msaccount.exception.InvalidDateFormatException;
import com.bank.msaccount.exception.InvalidDateRangeException;
import com.bank.msaccount.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Implementation of the ReportService interface
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final CustomerClient customerClient;

    private final ReportRepository reportRepository;

    @Override
    public Flux<ReportResponseDto> generateReport(
            Long customerId,
            String startDate,
            String endDate
    ) {

        log.info("Start generate report. customerId = {}, startDate = {}, endDate = {}",
                customerId, startDate, endDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(startDate, formatter);
            end = LocalDate.parse(endDate, formatter);
        } catch (DateTimeParseException ex) {
            throw new InvalidDateFormatException("Date format must be dd-MM-yyyy");
        }

        if (start.isAfter(end)) {
            throw new InvalidDateRangeException("startDate must be before or equal to endDate");
        }

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        return customerClient.findCustomerById(customerId)
                .flatMapMany(customer -> reportRepository
                        .findByCustomerId(customerId, startDateTime, endDateTime)
                        .map(report -> ReportResponseDto.builder()
                                .movementDate(report.getMovementDate())
                                .name(customer.getName())
                                .accountNumber(report.getAccountNumber())
                                .accountType(AccountType.valueOf(report.getAccountType()))
                                .previousBalance(report.getPreviousBalance())
                                .status(report.getStatus())
                                .amount(report.getAmount())
                                .movementType(MovementType.valueOf(report.getMovementType()))
                                .currentBalance(report.getCurrentBalance())
                                .build())
                )
                .doOnComplete(() ->
                        log.info("End generate report. customerId = {}, startDate = {}, endDate = {}",
                                customerId, start, end));
    }
}
