package com.currencycheckerapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripEntryDTO {

    private String title;
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;
    private ZonedDateTime dateFound;
    private String priceBig;
    private String priceSmall;
    private String link;
    private List<String> typeList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripEntryDTO that = (TripEntryDTO) o;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}
