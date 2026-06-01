package com.bank.msaccount.repository;

import com.bank.msaccount.dto.ReportQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<ReportQueryDto> findByCustomerId(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = """
                    SELECT
                        m.movement_date,
                        m.previous_balance,
                        m.amount,
                        m.current_balance,
                        m.type AS "movement_type",
                        m.status,
                        a.number AS "account_number",
                        a.type AS "account_type"
                    FROM movement m
                    INNER JOIN account a
                        ON a.id = m.account_id
                    WHERE
                        a.customer_id = :customerId
                        AND m.movement_date BETWEEN :startDate AND :endDate
                    ORDER BY
                        m.movement_date
                """;
        return databaseClient.sql(sql)
                .bind("customerId", customerId)
                .bind("startDate", startDate)
                .bind("endDate", endDate)
                .map((row, metadata) -> ReportQueryDto.builder()
                        .movementDate(row.get("movement_date", LocalDateTime.class))
                        .previousBalance(row.get("previous_balance", BigDecimal.class))
                        .amount(row.get("amount", BigDecimal.class))
                        .currentBalance(row.get("current_balance", BigDecimal.class))
                        .movementType(row.get("movement_type", String.class))
                        .status(row.get("status", Boolean.class))
                        .accountNumber(row.get("account_number", String.class))
                        .accountType(row.get("account_type", String.class))
                        .build())
                .all();
    }
}
