package com.currencycheckerapi.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class TripEntryDTO {

    private String title;
    private String dateFrom;
    private String dateTo;
    private String priceBig;
    private String priceSmall;
    private List<String> typeList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripEntryDTO that = (TripEntryDTO) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(dateFrom, that.dateFrom) &&
                Objects.equals(dateTo, that.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, dateFrom, dateTo);
    }
}
