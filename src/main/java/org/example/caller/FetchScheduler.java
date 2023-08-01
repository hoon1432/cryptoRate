package org.example.caller;

import org.example.constants.Currencies;
import org.example.entities.CurrencyRate;
import org.example.entities.CurrencyRateRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class FetchScheduler {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApiDataProvider apiDataProvider;

    @Autowired
    private CurrencyRateRepo currencyRateRepo;

    private String uri_noparam = "https://api.coinbase.com/v2/exchange-rates?currency=";

    @Scheduled(fixedRate = 5000)
    public void fetchLatestCurrency() {
        loadRates(Currencies.fiatCurrencies, Currencies.cryptoCurrencies);
        loadRates(Currencies.cryptoCurrencies,Currencies.fiatCurrencies);
    }

    private void loadRates(String[] baseRate, String[] convertRate) {
        for (String baseCurr: baseRate) {
            String convertResult = callApi(baseCurr);
            JSONObject convertObject = new JSONObject(convertResult);
            insertBaseCurrRates(convertObject,baseCurr,convertRate);
        }

    }

    private void insertBaseCurrRates(JSONObject data, String baseRate, String[] convertRates) {
        JSONObject rates = data.getJSONObject("data").getJSONObject("rates");
        long timestamp = System.currentTimeMillis();

        for (String convertRate: convertRates) {
            currencyRateRepo.save(new CurrencyRate(baseRate,convertRate,timestamp,processFloat(rates.getFloat(convertRate))));
        }

    }

    private String callApi(String currency) {
        String uri = uri_noparam + currency;
        return restTemplate.getForObject(uri,String.class);
    }

    private String processFloat(float value) {
        BigDecimal val = new BigDecimal(value);
        if (value < 1) {
            val = val.round(new MathContext(2));
        } else {
            val = val.setScale(2, RoundingMode.HALF_EVEN);
        }
        return val.toPlainString();
    }
}
