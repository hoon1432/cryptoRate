package org.example.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CurrencyRate {
    private @Id @GeneratedValue Long id;
    private String baseCurrency;
    private String targetCurrency;
    private Long timestamp;
    private String value;

    public CurrencyRate() {

    }

    public CurrencyRate(String baseCurrency, String targetCurrency, long timestamp, String value) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }

}
