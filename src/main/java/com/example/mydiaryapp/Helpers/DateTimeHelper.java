package com.example.mydiaryapp.Helpers;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeHelper {
    public static String getTimeDifference(LocalDateTime lastChange) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastChange, now);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + " sec ago";
        }

        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + " min ago";
        }

        long hours = duration.toHours();
        if (hours < 24) {
            return hours + (hours == 1 ? " hour" : " hours") + " ago";
        }

        long days = duration.toDays();
        return days + (days == 1 ? " day" : " days") + " ago";
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
