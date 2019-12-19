package com.currencycheckerapi.dao.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.currencycheckerapi.CurrencyCheckerApiApplication.getZoneId;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Long> {

    @Override
    public Long convertToDatabaseColumn(ZonedDateTime attribute) {
        return attribute.toEpochSecond();
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Long dbData) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(dbData), ZoneId.of(getZoneId()));
    }
}
