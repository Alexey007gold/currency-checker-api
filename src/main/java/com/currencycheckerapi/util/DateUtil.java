package com.currencycheckerapi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static final List<String> MONTHS = Arrays.asList("січня", "лютого", "березня", "квітня",
            "травня", "червня", "липня", "серпня", "вересня", "жовтня", "листопада", "грудня");

    private DateUtil() {
    }

    public static LocalDateTime parseDate(String date) throws ParseException {
        String[] split = date.split(" ");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        Date time = simpleDateFormat.parse(split[2]);

        int dayOfMonth = Integer.parseInt(split[0]);
        int month = MONTHS.indexOf(split[1]) + 1;
        int year;
        if (month >= LocalDate.now().getMonthValue() &&
                dayOfMonth >= LocalDate.now().getDayOfMonth() &&
                time.getHours() >= LocalTime.now().getHour()  &&
                time.getMinutes() >= LocalTime.now().getMinute()) {
            year = LocalDate.now().getYear();
        } else {
            year = LocalDate.now().getYear() + 1;
        }
        return LocalDateTime.of(LocalDate.of(year, month, dayOfMonth),
                LocalTime.of(time.getHours(), time.getMinutes()));
    }
}
