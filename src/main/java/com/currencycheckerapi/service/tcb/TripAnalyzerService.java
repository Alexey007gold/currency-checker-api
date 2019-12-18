package com.currencycheckerapi.service.tcb;

import com.currencycheckerapi.model.TripEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class TripAnalyzerService {

    @Autowired
    private TripDataCollectorService tripDataCollectorService;

    @Value("${zoneid}")
    private String zoneId;

    public List<List<TripEntryDTO>> findSubsequentTrips(int desiredTotalDays, int minTotalDays, int minHoursBetweenTrips, int maxHoursBetweenTrips) {
        List<TripEntryDTO> tripsFromNow = filter(ZonedDateTime.now(ZoneId.of(zoneId)), ZonedDateTime.of(LocalDateTime.MAX, ZoneId.of(zoneId)));
        List<List<TripEntryDTO>> results = new ArrayList<>();
        for (int i = 0; i < tripsFromNow.size(); i++) {
            results.addAll(getOptions(desiredTotalDays, minTotalDays, minHoursBetweenTrips, maxHoursBetweenTrips, tripsFromNow,
                    new ArrayList<>(List.of(tripsFromNow.get(i))), 0));
        }
        return results.stream().distinct().collect(toList());
    }

    private List<List<TripEntryDTO>> getOptions(int desiredTotalDays, int minTotalDays, int minHoursBetweenTrips, int maxHoursBetweenTrips,
                            List<TripEntryDTO> tripsFromNow, List<TripEntryDTO> startingPoint, int startingPointIdx) {
        long currentDuration = Duration.between(startingPoint.get(0).getDateFrom(), startingPoint.get(startingPoint.size() - 1).getDateTo()).toDays();
        if (currentDuration > desiredTotalDays)
            return currentDuration >= minTotalDays - 1 ? List.of(startingPoint) : List.of();

        List<List<TripEntryDTO>> results = new ArrayList<>();
        for (int i = startingPointIdx + 1; i < tripsFromNow.size(); i++) {
            long hoursBetween = Duration.between(startingPoint.get(startingPoint.size() - 1).getDateTo(),
                    tripsFromNow.get(i).getDateFrom()).toHours();
            if (hoursBetween >= minHoursBetweenTrips && hoursBetween <= maxHoursBetweenTrips &&
                Duration.between(startingPoint.get(0).getDateFrom(), tripsFromNow.get(i).getDateTo()).toDays() <= desiredTotalDays) {
                ArrayList<TripEntryDTO> dtos = new ArrayList<>(startingPoint);
                dtos.add(tripsFromNow.get(i));
                results.addAll(getOptions(desiredTotalDays, minTotalDays, minHoursBetweenTrips, maxHoursBetweenTrips, tripsFromNow,
                        dtos, i));
            }
        }
        if (results.isEmpty()) return currentDuration >= minTotalDays - 1 ? List.of(startingPoint) : List.of();
        return results;
    }

    public Map<String, Integer> getTotalCost(ZonedDateTime fromDate, ZonedDateTime toDate) {
        return filter(fromDate, toDate).stream()
                .collect(toMap(t -> t.getPriceSmall().split(" ")[1],
                        t -> Integer.parseInt(t.getPriceSmall().split(" ")[0]),
                        Integer::sum));
    }

    private List<TripEntryDTO> filter(ZonedDateTime fromDate, ZonedDateTime toDate) {
        if (fromDate == null) fromDate = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of(zoneId));
        if (toDate == null) toDate = ZonedDateTime.of(LocalDateTime.MAX, ZoneId.of(zoneId));
        ZonedDateTime finalFromDate = fromDate;
        ZonedDateTime finalToDate = toDate;
        return tripDataCollectorService.getTripList().stream()
                .filter(t -> t.getDateFrom().isAfter(finalFromDate) || t.getDateFrom().isEqual(finalFromDate) &&
                        t.getDateTo().isBefore(finalToDate) || t.getDateTo().isEqual(finalToDate)).collect(toList());
    }

}
