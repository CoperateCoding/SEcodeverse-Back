package com.coperatecoding.secodeverseback.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalTime locTime) {
        return (locTime == null ? null : locTime.toString());
    }

    @Override
    public LocalTime convertToEntityAttribute(String sqlTime) {
        return (sqlTime == null ? null : LocalTime.parse(sqlTime));
    }
}
