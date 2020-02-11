package com.soleeklab.nilebot.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.soleeklab.nilebot.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    public static String dateIn12 = "K:mm a";

    public static String displayTimeZone(TimeZone tz) {

        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);

        String result = "";


        if (hours >= 0) {
            result = String.format(Locale.US, "(GMT+%02d:%02d) %s", hours, minutes, tz.getID());
        } else {
            result = String.format(Locale.US, "(GMT%03d:%02d) %s", hours, minutes, tz.getID());
        }

        return result;

    }

    public static String formatDate(String dateInString, String currentFormat, String desiredFormat, String local) {
        Locale locale = new Locale(local);
        SimpleDateFormat format = new SimpleDateFormat(currentFormat);
        SimpleDateFormat targetTime = new SimpleDateFormat(desiredFormat, locale);

        Date date = null;
        try {
            date = format.parse(dateInString);
            String formattedTime = targetTime.format(date);
            return formattedTime;

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentTimeIn12Format() {
        Locale locale = new Locale("ar");
        SimpleDateFormat sdf = new SimpleDateFormat(dateIn12, locale);
        return sdf.format(new Date());
    }


    public static Long ChangeNumLocale(long num) {


        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US")); //or "nb","No" - for Norway
        return Long.parseLong(nf.format(num));

    }

}
