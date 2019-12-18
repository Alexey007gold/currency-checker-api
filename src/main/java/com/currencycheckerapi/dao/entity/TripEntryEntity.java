package com.currencycheckerapi.dao.entity;

import com.currencycheckerapi.dao.api.AbstractEntity;
import com.currencycheckerapi.dao.converter.TypeListConverter;
import com.currencycheckerapi.dao.converter.ZonedDateTimeConverter;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trip_entry")
@EqualsAndHashCode(callSuper = false)
@Builder
public class TripEntryEntity extends AbstractEntity {

    @Column(name = "title", nullable = false)
    private String title;
    @Convert(converter = ZonedDateTimeConverter.class)
    @Column(name = "date_from", nullable = false)
    private ZonedDateTime dateFrom;
    @Convert(converter = ZonedDateTimeConverter.class)
    @Column(name = "date_to", nullable = false)
    private ZonedDateTime dateTo;
    @Convert(converter = ZonedDateTimeConverter.class)
    @Column(name = "date_found", nullable = false)
    private ZonedDateTime dateFound;
    @Column(name = "price_big", nullable = false)
    private String priceBig;
    @Column(name = "price_small", nullable = false)
    private String priceSmall;
    @Convert(converter = TypeListConverter.class)
    @Column(name = "type_list", nullable = false)
    private List<String> typeList;
}
