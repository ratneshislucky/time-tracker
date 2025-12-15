package com.example.timetracker.service;

public final class TimeFormatting {
    private TimeFormatting() {
    }

    public static String formatMinutes(long minutes) {
        long hours = minutes / 60;
        long mins = minutes % 60;
        if (hours == 0) {
            return mins + "m";
        }
        return hours + "h " + mins + "m";
    }
}

