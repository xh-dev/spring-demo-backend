package me.xethh.libs.web;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class MongoConverters {

    @WritingConverter
    public enum OffsetDateTimeToStringConverter implements Converter<OffsetDateTime, String> {
        INSTANCE;
        @Override
        public String convert(OffsetDateTime source) {
            return source.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }

    @ReadingConverter
    public enum StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {
        INSTANCE;
        @Override
        public OffsetDateTime convert(String source) {
            return OffsetDateTime.parse(source, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @WritingConverter
    public enum LocalDateToStringConverter implements Converter<LocalDate, String> {
        INSTANCE;
        @Override
        public String convert(LocalDate source) {
            return source.format(DATE_FORMATTER);
        }
    }

    @ReadingConverter
    public enum StringToLocalDateConverter implements Converter<String, LocalDate> {
        INSTANCE;
        @Override
        public LocalDate convert(String source) {
            return LocalDate.parse(source, DATE_FORMATTER);
        }
    }
}