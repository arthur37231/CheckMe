package comp5216.sydney.edu.au.checkme.utils;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * All utilities related to date and time are defined in the class.
 */
public class DateTimeUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    /**
     * The single entry to get preset timezone. Time zone unifies all task deadline and remaining
     * time. Prevent system faults if the users' time zone changed.
     *
     * @return The preset time zone.
     */
    public static TimeZone getDefaultTimeZone() {
        return getSydneyTimeZone();
    }

    /**
     * Sydney Time Zone.
     *
     * @return A TimeZone object represents GMT+10:00 (Sydney).
     */
    public static TimeZone getSydneyTimeZone() {
        return TimeZone.getTimeZone("GMT+10:00");
    }

    /**
     * China Time Zone.
     *
     * @return A TimeZone object represents GMT+08:00 (China).
     */
    public static TimeZone getChinaTimeZone() {
        return TimeZone.getTimeZone("GMT+08:00");
    }

    /**
     * Obtain a formatted string to represent the provided date.
     *
     * @param dateTime A {@link Calendar} object containing date information.
     * @param format A String object contains placeholders to place date fields.
     * @return A formatted date in string type and it satisfies provided format.
     */
    public static String formatDate(Calendar dateTime, String format) {
        return String.format(
                format,
                dateTime.get(Calendar.DAY_OF_MONTH),
                dateTime.get(Calendar.MONTH) + 1,
                dateTime.get(Calendar.YEAR)
        );
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    /**
     * Obtain a formatted string to represent te provided time.
     *
     * @param dateTime A {@link Calendar} object containing time information.
     * @param format A string object contains placeholders to place time fields.
     * @return A formatted time in string type and it satisfies provided format.
     */
    public static String formatTime(Calendar dateTime, String format) {
        return String.format(
                format,
                dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE)
        );
    }

    /**
     * Obtain current date and time in the preset time zone;
     *
     * @return A {@link Calendar} object contains current time information.
     */
    public static Calendar getToday() {
        return Calendar.getInstance(getDefaultTimeZone());
    }

    /**
     * Convert a {@link Calendar} object to {@link Long} object representing the time in
     * milliseconds.
     *
     * @param dateTime A Calendar object contains date and time information.
     * @return A Long object represents date and time in milliseconds.
     */
    @TypeConverter
    public static Long toTimeStamp(Date dateTime) {
        return dateTime == null ? null : dateTime.getTime();
    }

    /**
     * Convert a {@link Long} object representing time in milliseconds to {@link Calendar} object.
     *
     * @param timestamp A Long object represents date and time in milliseconds.
     * @return A Calendar object contains date and time information.
     */
    @TypeConverter
    public static Date toDateTime(Long timestamp) {
        if(timestamp == null) return null;
        Date dateTime = new Date();
        dateTime.setTime(timestamp);
        return dateTime;
    }
}
