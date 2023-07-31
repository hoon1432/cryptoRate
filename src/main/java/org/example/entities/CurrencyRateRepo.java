package org.example.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrencyRateRepo extends CrudRepository<CurrencyRate, Long> {

    @Query("SELECT r FROM CurrencyRate r WHERE r.baseCurrency = :baseCurr " +
            "and r.targetCurrency = :targetCurr " +
            "and timestamp=(SELECT max(timestamp) FROM CurrencyRate r WHERE r.baseCurrency = :baseCurr)")
    CurrencyRate findLatestRecordForCurrencyPair(
            @Param("baseCurr") String baseCurrency,
            @Param("targetCurr") String targetCurrency);

    @Query("SELECT r FROM CurrencyRate r WHERE r.baseCurrency = :baseCurr " +
            "and r.targetCurrency = :targetCurr " +
            "and r.timestamp >= :startTime " +
            "and r.timestamp < :endTime")
    List<CurrencyRate> findRatesBetweenTimestamp(
            @Param("baseCurr") String baseCurrency,
            @Param("targetCurr") String targetCurrency,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

}
