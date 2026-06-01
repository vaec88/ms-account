package com.bank.msaccount.repository;

import com.bank.msaccount.dto.ReportQueryDto;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Repository interface for report operations
 */
public interface ReportRepository {

    /**
     * Finds all movements by customer ID and date range
     *
     * @param customerId the ID of the customer
     * @param startDate  the start date of the movements
     * @param endDate    the end date of the movements
     * @return a Flux of the movements {@link ReportQueryDto}
     */
    Flux<ReportQueryDto> findByCustomerId(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate);
}
