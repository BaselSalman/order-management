package com.birzeit.ordermanagementapi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {

    static DateTimeFormatter dateAndTimeObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    static DateTimeFormatter dateObj = DateTimeFormatter.ofPattern("yyyy/MM/dd");


    public static String formatDateAndTime (LocalDateTime dateAndTime) {
        if(dateAndTime != null)
            return dateAndTime.format(dateAndTimeObj);
        return null;
    }

    public static String formatDate (LocalDate localDate) {
        if(localDate != null)
            return localDate.format(dateObj);
        return null;
    }

    public static LocalDate makeDateFromString (String date) {
        return LocalDate.parse(date, dateObj);
    }

    public static LocalDateTime makeDateTimeFromString(String dateAndTime) {
        return LocalDateTime.parse(dateAndTime, dateAndTimeObj);
    }

}