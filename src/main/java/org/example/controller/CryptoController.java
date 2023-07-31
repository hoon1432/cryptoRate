package org.example.controller;

import org.example.caller.DbDataProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CryptoController {

    @Autowired
    private DbDataProvider dbDataProvider;

    @GetMapping(path="/exchange-rates", produces = "application/json")
    public ResponseEntity<String> getExchangeRate(@RequestParam(value = "base", defaultValue = "") String base){

        if ("fiat".equalsIgnoreCase(base)) {
            String response = dbDataProvider.getFiatRates().toString();
            return ResponseEntity.ok().body(response);
        } else if ("crypto".equalsIgnoreCase(base)) {
            String response = dbDataProvider.getCryptoRates().toString();
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Base is invalid.");
        }

    }

    @GetMapping(path="/historical-rates",produces = "application/json")
    public ResponseEntity<String> getHistoricalRates(
            @RequestParam(value = "base_currency", defaultValue = "") String baseCurr,
            @RequestParam(value = "target_currency", defaultValue = "") String targetCurr,
            @RequestParam(value = "start") long startTime,
            @RequestParam(value = "end", required = false) Long endTime) {

        JSONObject response;

        if (Objects.isNull(endTime)) {
            response = dbDataProvider.getHistoricalRates(baseCurr,targetCurr,startTime);
        } else {
            response = dbDataProvider.getHistoricalRates(baseCurr,targetCurr,startTime,endTime);
        }

        return ResponseEntity.ok().body(response.toString());
    }
    
}
