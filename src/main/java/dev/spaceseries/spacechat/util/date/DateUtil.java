package dev.spaceseries.spacechat.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class DateUtil {

    private static final String DATE_SERIALIZATION_FORMAT = "yyyy-MM-dd HH:mm.SSS";

    public static String toString(Date date) {
        return new SimpleDateFormat(DATE_SERIALIZATION_FORMAT).format(date);
    }

    public static Date fromString(String dateString) {
        try {
            return new SimpleDateFormat(DATE_SERIALIZATION_FORMAT).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Convert text like ["1d"] to [1 day as a date]
     *
     * @param dateString The text
     * @return The length of time
     */
    public static Long fromStringFormatToDurationMillis(String dateString) {
        try {
            if (dateString.equalsIgnoreCase("now"))
                return 0L;
            return Duration.parse(dateString).toMillis();
        } catch (Exception e) {
            return null;
        }
    }
}

