package com.currencycheckerapi.service.provider;

import com.currencycheckerapi.model.MonoDTO;
import com.currencycheckerapi.service.CurrencyCodeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
public class MonoRateProvider implements RateProvider {

    @Autowired
    private CurrencyCodeService currencyCodeService;

    @Autowired
    private ObjectMapper mapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.monobank.ua/bank/currency"))
            .build();

    private List<MonoDTO> rateList;

    @Override
    public String getRate(String currencyA, String currencyB) {
        try {
            MonoDTO monoDTO = rateList.stream()
                    .filter(e -> currencyCodeService.getCodeForName(currencyA) == e.getCurrencyCodeA() &&
                            currencyCodeService.getCodeForName(currencyB) == e.getCurrencyCodeB())
                    .findFirst().orElseThrow();
            return mapper.writeValueAsString(monoDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void fetchRate() throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        rateList = mapper.readValue(httpResponse.body(), new TypeReference<List<MonoDTO>>() {});
        log.info("Fetched mono data");
    }

    @Override
    public String getProviderName() {
        return "mono";
    }
}
