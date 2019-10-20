package com.currencycheckerapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class SelfWakeUpService {

    @Autowired
    private HttpClient httpClient;

    @Value("${deployment.url}")
    private String url;

    private HttpRequest request;

    @PostConstruct
    public void init() {
        request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/health"))
                .build();
    }

    @Scheduled(fixedRate = 14 * 60000)
    public void callHealth() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("sent wake up request: " + response.body());
    }
}
