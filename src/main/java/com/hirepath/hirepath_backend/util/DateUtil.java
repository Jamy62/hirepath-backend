package com.hirepath.hirepath_backend.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateUtil {
    public static ZonedDateTime convertTimestampToZDT(Object timestampObj) {
        if (timestampObj instanceof java.sql.Timestamp) {
            java.sql.Timestamp timestamp = (java.sql.Timestamp) timestampObj;
            return timestamp.toInstant().atZone(ZoneId.systemDefault());
        } else if (timestampObj instanceof ZonedDateTime) {
            return ((ZonedDateTime) timestampObj);
        }else if (timestampObj instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) timestampObj;
            return localDateTime.atZone(ZoneId.systemDefault());
        }else if (timestampObj instanceof String) {
            String stringDateTime = (String) timestampObj;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSX");
            return ZonedDateTime.parse(stringDateTime, formatter);
        }
        else {
            log.error("Unexpected type for timestampObj: {}", timestampObj.getClass());
            return null;
        }
    }
}
