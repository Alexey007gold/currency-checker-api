package com.currencycheckerapi.service.tcb;

import com.currencycheckerapi.dao.entity.TripEntryEntity;
import com.currencycheckerapi.dao.service.TripEntryService;
import com.currencycheckerapi.model.TripEntryDTO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.currencycheckerapi.CurrencyCheckerApiApplication.getZoneId;
import static com.currencycheckerapi.util.DateUtil.parseDate;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class TripDataCollectorService {

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private TripEntryService tripEntryService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${zoneid}")
    private String zoneId;

    private List<TripEntryDTO> tripList;

    private final HttpRequest calendarRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://tcb.com.ua/calendar/"))
            .build();
    private Comparator<TripEntryDTO> comparator;

    @PostConstruct
    public void init() {
        log.info(LocalDateTime.now().toString());
        log.info(ZonedDateTime.now(ZoneId.of(getZoneId())).toString());
        comparator = Comparator.comparing(TripEntryDTO::getDateFrom)
                .thenComparing(TripEntryDTO::getTitle);
        tripList = StreamSupport.stream(tripEntryService.findAll().spliterator(), false)
                .map(e -> modelMapper.map(e, TripEntryDTO.class))
                .sorted(comparator)
                .collect(toList());
    }

    public List<TripEntryDTO> collect() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        HttpResponse<String> httpResponse = httpClient.send(calendarRequest, HttpResponse.BodyHandlers.ofString());
        String calendarHtml = httpResponse.body();
        Document doc = Jsoup.parse(calendarHtml);
        Element calorders = doc.getElementById("calorders");
        Element tbody = calorders.getElementsByTag("tbody").get(0);
        Elements travelEntries = tbody.children();
        List<String> travelEntryUrls = travelEntries.stream()
                .map(e -> e.getElementsByTag("a").get(0).attr("href"))
                .collect(toList());
        List<TripEntryDTO> tripEntryDTOList = new ArrayList<>();
        travelEntryUrls.stream().parallel().forEach(u -> {
            try {
                HttpRequest tripRequest = HttpRequest.newBuilder()
                        .uri(URI.create(String.format("https://tcb.com.ua%s", u)))
                        .build();
                String tripHtml = httpClient.send(tripRequest, HttpResponse.BodyHandlers.ofString()).body();
                Document document = Jsoup.parse(tripHtml);
                Elements ordertheadTable = document.getElementById("orderthead").getElementsByTag("tr").get(0).children();
                String from = ordertheadTable.get(0).childNodes().get(0).toString().trim();
                String to = ordertheadTable.get(2).childNodes().get(0).toString().trim();
                String priceBig = ordertheadTable.get(4).children().get(0).text();
                String priceSmall = ordertheadTable.get(4).children().get(2).text();
                String title = document.getElementsByClass("tourtext").get(0).getElementsByTag("h1").text().trim();
                Elements typeChildren = document.getElementsByClass("tourtext").get(0).getElementsByTag("span").get(0).children();
                List<String> typeList = new ArrayList<>();
                for (int i = 1; i < typeChildren.size(); i++) {
                    typeList.add(typeChildren.get(i).text());
                }
                tripEntryDTOList.add(TripEntryDTO.builder()
                        .title(title)
                        .dateFrom(parseDate(from, zoneId))
                        .dateTo(parseDate(to, zoneId))
                        .dateFound(ZonedDateTime.now(ZoneId.of(getZoneId())))
                        .priceBig(priceBig)
                        .priceSmall(priceSmall)
                        .typeList(typeList)
                        .build());
            } catch (InterruptedException | IOException e) {
                log.error(String.format("Exception message: %s, Stacktrace: %s",
                        e.getMessage(), Arrays.toString(e.getStackTrace())));
            }
        });
        log.info(String.format("Time spent for trips fetching %ss", (System.currentTimeMillis() - start) / 1000.));
        return tripEntryDTOList;
    }

    @Scheduled(fixedRate = 600_000)
    @Transactional
    public void fetchTrips() throws IOException, InterruptedException {
        List<TripEntryDTO> updatedData = collect();
        updatedData.removeAll(tripList);
        log.info(String.format("Found %s new trip(s)", updatedData.size()));

        List<TripEntryEntity> newEntities = updatedData.stream()
                .map(e -> modelMapper.map(e, TripEntryEntity.class))
                .collect(toList());
        tripEntryService.save(newEntities);
        tripList.addAll(updatedData);
        tripList.sort(comparator);
    }

    public Comparator<TripEntryDTO> getComparator() {
        return comparator;
    }

    public List<TripEntryDTO> getTripList() {
        return tripList;
    }
}
