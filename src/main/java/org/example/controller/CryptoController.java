package org.example.controller;

import org.example.caller.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {

    @Autowired
    private DataFetcher dataFetcher;

    @GetMapping(path="/exchange-rates", produces = "application/json")
    public ResponseEntity<String> getExchangeRate(@RequestParam(value = "base", defaultValue = "") String base){

        if ("fiat".equalsIgnoreCase(base)) {
            String response = dataFetcher.getFiatRates();
            return ResponseEntity.ok().body(response);
        } else if ("crypto".equalsIgnoreCase(base)) {
            String response = dataFetcher.getCryptoRates();
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Base is invalid.");
        }

    }

    private boolean isValidBase(String base) {
        return "fiat".equalsIgnoreCase(base) || "crypto".equalsIgnoreCase(base);
    }

}