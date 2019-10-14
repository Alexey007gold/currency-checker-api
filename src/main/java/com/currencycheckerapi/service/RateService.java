package com.currencycheckerapi.service;

import com.currencycheckerapi.service.provider.RateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
public class RateService {

    private Map<String, RateProvider> rateProviderMap;

    @Autowired
    public RateService(List<RateProvider> rateProviders) {
        rateProviderMap = rateProviders.stream().collect(toMap(RateProvider::getProviderName, Function.identity()));
    }

    public String getRate(String provider, String currencyA, String currencyB) {
        return rateProviderMap.get(provider).getRate(currencyA, currencyB);
    }
}
