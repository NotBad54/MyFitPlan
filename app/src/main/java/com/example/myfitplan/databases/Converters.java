package com.example.myfitplan.databases;

import androidx.room.TypeConverter;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class Converters {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @TypeConverter
    public static LocalDate fromString(String value) {
        return (value == null || value.isEmpty()) ? null : LocalDate.parse(value, DATE_FORMATTER);
    }

    @TypeConverter
    public static String dateToString(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMATTER);
    }
}