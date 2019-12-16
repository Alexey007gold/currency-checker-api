package com.currencycheckerapi.dao.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class TypeListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return List.of(dbData.split(","));
    }
}
