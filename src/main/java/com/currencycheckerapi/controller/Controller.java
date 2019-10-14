package com.currencycheckerapi.controller;

import com.currencycheckerapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private RateService rateService;

    @GetMapping(value = "rate/{provider}/{currencyA}/{currencyB}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getRate(@PathVariable("provider") String provider,
                          @PathVariable("currencyA") String currencyA, @PathVariable("currencyB") String currencyB) {
        return rateService.getRate(provider, currencyA, currencyB);
    }
}
