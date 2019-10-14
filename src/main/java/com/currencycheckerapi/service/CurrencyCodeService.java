package com.currencycheckerapi.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class CurrencyCodeService {

    private Map<String, Integer> nameToNumCode;

    public CurrencyCodeService() throws IOException {
        List<String> lines = Files.readAllLines(new ClassPathResource("codes.txt").getFile().toPath());
        nameToNumCode = lines.stream()
                .map(l -> l.split("\t"))
                .collect(toMap(a -> a[1], a -> Integer.parseInt(a[2])));
    }

    public int getCodeForName(String name) {
        return nameToNumCode.get(name.toUpperCase());
    }
}
