package com.currencycheckerapi.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

public class DateUtil {

    private static final List<String> MONTHS = Arrays.asList("січня", "лютого", "березня", "квітня",
            "травня", "червня", "липня", "серпня", "вересня", "жовтня", "листопада", "грудня");

    private DateUtil() {
    }

    public static LocalDateTime parseDate(String date) {
        String[] split = date.split(" ");
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("HH:mm");
        TemporalAccessor time = simpleDateFormat.parse(split[2]);

        int dayOfMonth = Integer.parseInt(split[0]);
        int month = MONTHS.indexOf(split[1]) + 1;
        int year = LocalDateTime.now().getYear();
        LocalDateTime dateTimeNow = LocalDateTime.now().withYear(0).withSecond(0).withNano(0);
        LocalDateTime parsedDateTime = LocalDateTime.of(0, month, dayOfMonth, time.get(HOUR_OF_DAY), time.get(MINUTE_OF_HOUR));
        if (dateTimeNow.compareTo(parsedDateTime) >= 0) {
            year++;
        }
        return LocalDateTime.of(LocalDate.of(year, month, dayOfMonth),
                LocalTime.of(time.get(HOUR_OF_DAY), time.get(MINUTE_OF_HOUR)));
    }
}
