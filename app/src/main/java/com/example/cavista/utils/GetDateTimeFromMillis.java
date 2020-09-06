package com.example.cavista.utils;

import android.text.format.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Locale;

/**
 * Class to return time from timestamp provided
 */
public class GetDateTimeFromMillis {

    /**
     * This method return time difference for comment with current time
     *
     * @param timeInMillis : comment time in millis
     * @return : time elapsed after creating comment
     */
    @NotNull
    public static String convertToDate(Long timeInMillis) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timeInMillis);
        final CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                calendar.getTimeInMillis(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS);
        return relativeTime.toString();
    }
}
