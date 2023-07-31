package org.example.caller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class DataFetcher {

    private String uri_noparam = "https://api.coinbase.com/v2/exchange-rates?currency=";

    private String[] fiatCurrencies = {"SGD","USD","EUR"};
    private String[] cryptoCurrencies = {"BTC","DOGE","ETH"};

    // Call 3 currencies
    // Get 3 currencies
    
    public String getFiatRates() {
        return getRates(fiatCurrencies,cryptoCurrencies).toString();
    }

    public String getCryptoRates() {
        return getRates(cryptoCurrencies,fiatCurrencies).toString();
    }

    private JSONObject getRates(String[] baseRate, String[] convertRate) {
        JSONObject convertRates = new JSONObject();

        for (String fiatCur: baseRate) {
            String convertResult = callApi(fiatCur);
            JSONObject convertObject = new JSONObject(convertResult);
            JSONObject childRates = getChildRates(convertObject,convertRate);
            convertRates.put(fiatCur,childRates);
        }

        return convertRates;
    }

    private String callApi(String currency) {
        String uri = uri_noparam + currency;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri,String.class);
    }

    private JSONObject getChildRates(JSONObject data,String[] toRates) {

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
