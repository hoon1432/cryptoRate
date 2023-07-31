package org.example.caller;

import org.example.constants.Currencies;
import org.example.entities.CurrencyRate;
import org.example.entities.CurrencyRateRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
