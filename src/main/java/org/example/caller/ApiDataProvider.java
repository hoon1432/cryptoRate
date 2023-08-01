package org.example.caller;

import org.example.constants.Currencies;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class ApiDataProvider {
    @Autowired
    private RestTemplate restTemplate;

    private String uri_noparam = "https://api.coinbase.com/v2/exchange-rates?currency=";
    
    public JSONObject getFiatRates() {
        return getRates(Currencies.fiatCurrencies, Currencies.cryptoCurrencies);
    }

    public JSONObject getCryptoRates() {
        return getRates(Currencies.cryptoCurrencies,Currencies.fiatCurrencies);
    }

    private JSONObject getRates(String[] baseRate, String[] convertRate) {
        JSONObject convertRates = new JSONObject();

        for (String baseCurr: baseRate) {
            String convertResult = callApi(baseCurr);
            JSONObject convertObject = new JSONObject(convertResult);
            JSONObject targetRates = getTargetRates(convertObject,convertRate);
            convertRates.put(baseCurr,targetRates);
        }

        return convertRates;
    }

    private String callApi(String currency) {
        String uri = uri_noparam + currency;
        return restTemplate.getForObject(uri,String.class);
    }

    private JSONObject getTargetRates(JSONObject data, String[] toRates) {
        JSONObject toReturn = new JSONObject();
        JSONObject rates = data.getJSONObject("data").getJSONObject("rates");

        for (String rate: toRates) {
            toReturn.put(rate,processFloat(rates.getFloat(rate)));
        }

        return toReturn;

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
