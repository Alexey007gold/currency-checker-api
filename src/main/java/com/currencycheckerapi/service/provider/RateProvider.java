package com.currencycheckerapi.service.provider;

public interface RateProvider {

    String getRate(String currencyA, String currencyB);

    String getProviderName();
}
