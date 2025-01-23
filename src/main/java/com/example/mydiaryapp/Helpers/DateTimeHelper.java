package com.example.mydiaryapp.Helpers;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeHelper {
    public static String getTimeDifference(LocalDateTime startInclusive, LocalDateTime endInclusive, String word) {
        Duration duration = Duration.between(startInclusive, endInclusive);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + " sec " + word;
        }

        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + " min " + word;
        }

        long hours = duration.toHours();
        if (hours < 24) {
            return hours + (hours == 1 ? " hour" : " hours") + word;
        }

        long days = duration.toDays();
        return days + (days == 1 ? " day" : " days") + word;
    }

    public static boolean isDateBetween(LocalDateTime dateToCheck, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return true;
        }

        if (startDate == null) {
            return !dateToCheck.isAfter(endDate);
        }

        if (endDate == null) {
            return !dateToCheck.isBefore(startDate);
        }

        if (startDate.isAfter(endDate)) {
            LocalDateTime tmpDateTime = startDate;
            startDate = endDate;
            endDate = tmpDateTime;
        }

        return !dateToCheck.isBefore(startDate) && !dateToCheck.isAfter(endDate);
    }
}
