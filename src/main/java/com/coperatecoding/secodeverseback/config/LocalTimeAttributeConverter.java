package com.coperatecoding.secodeverseback.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
        return (locDateTime == null ? null : Timestamp.valueOf(locDateTime.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()));
    }

//    @Override
//    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
//        return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
//    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return (sqlTimestamp == null ? null : sqlTimestamp.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
    }


}