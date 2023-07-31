package org.example.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CurrencyRateRepo extends CrudRepository<CurrencyRate, Long> {

    @Query("SELECT r FROM CurrencyRate r WHERE r.baseCurrency = :baseCurr " +
            "and r.targetCurrency = :targetCurr " +
            "and timestamp=(SELECT max(timestamp) FROM CurrencyRate r WHERE r.baseCurrency = :baseCurr)")
    CurrencyRate findLatestRecordForCurrencyPair(
            @Param("baseCurr") String baseCurrency,
            @Param("targetCurr") String targetCurrency);



}
