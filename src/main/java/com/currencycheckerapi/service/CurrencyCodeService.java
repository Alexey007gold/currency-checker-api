package com.currencycheckerapi.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class CurrencyCodeService {

    private Map<String, Integer> nameToNumCode;

    public CurrencyCodeService() throws IOException {
        InputStream inputStream = new ClassPathResource("codes.tsv").getInputStream();
        nameToNumCode = new BufferedReader(new InputStreamReader(inputStream)).lines()
                .map(l -> l.split("\t"))
                .collect(toMap(a -> a[1], a -> Integer.parseInt(a[2])));
    }

    public int getCodeForName(String name) {
        return nameToNumCode.get(name.toUpperCase());
    }
}
