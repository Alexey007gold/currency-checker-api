package com.currencycheckerapi.controller;

import com.currencycheckerapi.model.TripEntryDTO;
import com.currencycheckerapi.service.tcb.TripDataCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TripController {

    @Autowired
    private TripDataCollectorService tripDataCollectorService;

    @GetMapping(value = "trips", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TripEntryDTO> getRate() {
        return tripDataCollectorService.getTripList();
    }
}
