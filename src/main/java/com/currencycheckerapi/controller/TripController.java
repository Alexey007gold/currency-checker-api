package com.currencycheckerapi.controller;

import com.currencycheckerapi.model.TripEntryDTO;
import com.currencycheckerapi.service.tcb.TripAnalyzerService;
import com.currencycheckerapi.service.tcb.TripDataCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TripController {

    @Autowired
    private TripDataCollectorService tripDataCollectorService;

    @Autowired
    private TripAnalyzerService tripAnalyzerService;

    @GetMapping(value = "trips", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TripEntryDTO> getTrips() {
        return tripDataCollectorService.getTripList();
    }

    @GetMapping(value = "tripTotalCost", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Integer> getTotalCost() {
        return tripAnalyzerService.getTotalCost(null, null);
    }

    @GetMapping(value = "analyzeTrips", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void analyze() {
        tripAnalyzerService.findSubsequentTrips(5, 5, 12, 24);
    }
}
