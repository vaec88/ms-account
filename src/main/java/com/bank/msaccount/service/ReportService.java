package com.bank.msaccount.service;

import com.bank.msaccount.dto.ReportResponseDto;
import reactor.core.publisher.Flux;

/**
 * Interface for report service
 */
public interface ReportService {

    /**
     * Generates a report for a specific customer
     *
     * @param customerId the ID of the customer
     * @param startDate  the start date of the movements
     * @param endDate    the end date of the movements
     * @return a Flux containing the account statement {@link ReportResponseDto}
     */
    Flux<ReportResponseDto> generateReport(Long customerId, String startDate, String endDate);
}
