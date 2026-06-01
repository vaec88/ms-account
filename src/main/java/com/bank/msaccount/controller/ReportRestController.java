package com.bank.msaccount.controller;

import com.bank.msaccount.dto.ReportResponseDto;
import com.bank.msaccount.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Rest controller for report operations
 */
@RestController
@RequestMapping("/reports")
@Validated
@RequiredArgsConstructor
public class ReportRestController {

    private final ReportService reportService;

    /**
     * Generates a report for a client
     *
     * @param clientId  the ID of the client
     * @param startDate the start date of the movements
     * @param endDate   the end date of the movements
     * @return a Flux containing the account statement {@link ReportResponseDto}
     */
    @GetMapping("/{clientId}")
    public Flux<ReportResponseDto> generateReport(
            @PathVariable Long clientId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return reportService.generateReport(clientId, startDate, endDate);
    }
}
