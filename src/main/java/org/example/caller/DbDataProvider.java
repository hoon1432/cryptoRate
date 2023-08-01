package org.example.caller;

import org.example.constants.Currencies;
import org.example.entities.CurrencyRate;
import org.example.entities.CurrencyRateRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbDataProvider {

    @Autowired
    private CurrencyRateRepo currencyRateRepo;

    public JSONObject getFiatRates() {
        return getRates(Currencies.fiatCurrencies, Currencies.cryptoCurrencies);
    }

    public JSONObject getCryptoRates() {
        return getRates(Currencies.cryptoCurrencies,Currencies.fiatCurrencies);
    }

    public JSONObject getHistoricalRates(String baseCurr, String targetCurr, long startTime) {
        return getHistoricalRates(baseCurr,targetCurr,startTime,System.currentTimeMillis());
    }

    public JSONObject getHistoricalRates(String baseCurr, String targetCurr, long startTime, long endTime) {
        List<CurrencyRate> getAllRates = currencyRateRepo.findRatesBetweenTimestamp(baseCurr,targetCurr,startTime,endTime);

        JSONObject rates = new JSONObject();
        JSONArray resultsArr = new JSONArray();

        for (CurrencyRate rate: getAllRates) {
            JSONObject timestampRate = new JSONObject();
            timestampRate.put("timestamp",rate.getTimestamp());
            timestampRate.put("value",rate.getValue());
            resultsArr.put(timestampRate);
        }
        rates.put("results",resultsArr);

        return rates;
    }

    private JSONObject getRates(String[] baseRate, String[] convertRate) {
        JSONObject baseRates = new JSONObject();

        for (String baseCurr: baseRate) {
            JSONObject convertRates = new JSONObject();
            for (String convertCurr: convertRate) {
                CurrencyRate rate = currencyRateRepo.findLatestRecordForCurrencyPair(baseCurr,convertCurr);
                convertRates.put(convertCurr,rate.getValue());
            }
            baseRates.put(baseCurr,convertRates);
        }

        return baseRates;
    }
}
