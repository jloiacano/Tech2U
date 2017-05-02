package com.example.android.newsapp2;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by J on 4/2/2017.
 * <p>
 * A re-usable {@link CustomTimeDateFormat} class to accept a string dateTimeStamp and, once
 * constructed in the code, can be accessed so that two separate TextViews can be set...
 * <p>
 * one from the method getTimePastAttribute() to return the number of units of time past in string
 * format.
 * <p>
 * the other from getUnitOfTimeOrDateStamp() to return the String Resource ID in integer format so
 * that the calling function can use SOME_VIEW.setText(SOME_OBJECT.getUnitOfTimeOrDateStamp());
 */

public class CustomTimeDateFormat {

    // A tag for the log messages
    private static final String LOG_TAG = CustomTimeDateFormat.class.getSimpleName();

    // The number of units of time that have past (ie the "5" in "5 minutes ago"
    private final String mTimePastAttribute;

    // The units of time that have past (ie the "minutes ago" in "5 minuts ago") or the formatted
    // DateStamp if it was over a week ago (can be adjusted in future uses for more units of time)
    private final int mUnitOfTimeOrDateStamp;


    public CustomTimeDateFormat(String dateTimeStamp) {

        Calendar suppliedCalendar = null;
        Calendar currentCalendar = null;

        try {
            // Format to a Date Object
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = simpleDateFormat.parse(dateTimeStamp);

            // Reformat the Date Object into a Calendar Object
            suppliedCalendar = Calendar.getInstance();
            suppliedCalendar.setTime(date);

            // Get the current time/date CalendarObject
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            currentCalendar = Calendar.getInstance(timeZone);
        } catch (Exception e) {
            Log.e(LOG_TAG, "problem parsing the date for number of time units", e);
        }

        // Use formatTimePastAttribute to set mTimePastAttribute from dateTimeStamp
        mTimePastAttribute = formatTimePastAttribute(suppliedCalendar, currentCalendar);

        //Use formatTheUnitOfTimeORDateStamp() to set the mUnitOfTimeOrDateStamp from dateTimeStamp
        mUnitOfTimeOrDateStamp = formatTheUnitOfTimeORDateStamp(suppliedCalendar, currentCalendar);
    }


    public String getTimePastAttribute() {
        return mTimePastAttribute;
    }

    public int getUnitOfTimeStringResourceID() {
        return mUnitOfTimeOrDateStamp;
    }


    public String formatTimePastAttribute(Calendar suppliedCalendar, Calendar currentCalendar) {
        String timePastAttribute = "";

        // Compare the published date/time to the current date/time
        if (suppliedCalendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR)) {

        } else if (suppliedCalendar.get(Calendar.WEEK_OF_YEAR) < currentCalendar.get(Calendar.WEEK_OF_YEAR) - 1) {

        } else if (suppliedCalendar.get(Calendar.DAY_OF_YEAR) < currentCalendar.get(Calendar.DAY_OF_YEAR)) {
            int numberOfDaysAgo = currentCalendar.get(Calendar.DAY_OF_YEAR) - suppliedCalendar.get(Calendar.DAY_OF_YEAR);
            timePastAttribute = Integer.toString(numberOfDaysAgo) + " ";

        } else if (suppliedCalendar.get(Calendar.HOUR_OF_DAY) < currentCalendar.get(Calendar.HOUR_OF_DAY)) {
            int numberOfHoursAgo = currentCalendar.get(Calendar.HOUR_OF_DAY) - suppliedCalendar.get(Calendar.HOUR_OF_DAY);
            timePastAttribute = Integer.toString(numberOfHoursAgo) + " ";

        } else if (suppliedCalendar.get(Calendar.MINUTE) < currentCalendar.get(Calendar.MINUTE)) {
            int numberOfMinutesAgo = currentCalendar.get(Calendar.MINUTE) - suppliedCalendar.get(Calendar.MINUTE);
            timePastAttribute = Integer.toString(numberOfMinutesAgo) + " ";
        } else {
            timePastAttribute = "NOW";
        }

        return timePastAttribute;
    }


    public int formatTheUnitOfTimeORDateStamp(Calendar suppliedCalendar, Calendar currentCalendar) {

        int stringResourceIdToReturn = R.string.no_resource;

        if (suppliedCalendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR)) {

        } else if (suppliedCalendar.get(Calendar.WEEK_OF_YEAR) < currentCalendar.get(Calendar.WEEK_OF_YEAR) -1) {

        } else if (currentCalendar.get(Calendar.DAY_OF_YEAR) - suppliedCalendar.get(Calendar.DAY_OF_YEAR) == 1) {
            stringResourceIdToReturn = R.string.day_ago;

        } else if (suppliedCalendar.get(Calendar.DAY_OF_YEAR) < currentCalendar.get(Calendar.DAY_OF_YEAR)) {
            stringResourceIdToReturn = R.string.days_ago;

        } else if (currentCalendar.get(Calendar.HOUR_OF_DAY) - suppliedCalendar.get(Calendar.HOUR_OF_DAY) == 1) {
            stringResourceIdToReturn = R.string.hour_ago;

        } else if (suppliedCalendar.get(Calendar.HOUR_OF_DAY) < currentCalendar.get(Calendar.HOUR_OF_DAY)) {
            stringResourceIdToReturn = R.string.hours_ago;

        } else if (currentCalendar.get(Calendar.MINUTE) - suppliedCalendar.get(Calendar.MINUTE) == 1) {
            stringResourceIdToReturn = R.string.minute_ago;

        } else if (suppliedCalendar.get(Calendar.MINUTE) < currentCalendar.get(Calendar.MINUTE)) {
            stringResourceIdToReturn = R.string.minutes_ago;
        }

        return stringResourceIdToReturn;
    }
}