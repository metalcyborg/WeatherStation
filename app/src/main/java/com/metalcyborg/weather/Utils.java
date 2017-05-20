package com.metalcyborg.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by metalcyborg on 19.05.17.
 */

public class Utils {

    public static String convertLongToDateString(long milliseconds) {
        if(milliseconds < 0) {
            return "";
        }

        return new SimpleDateFormat("EEE, MMM d", new Locale("en")).format(new Date(milliseconds));
    }

    public static String getTemperatureString(float temp) {
        String tempStr = String.valueOf(Math.round(temp));
        if (temp < 0) {
            tempStr = "-" + tempStr;
        } else if (temp > 0) {
            tempStr = "+" + tempStr;
        }

        return tempStr;
    }

    public static String convertLongToTimeString(long milliseconds) {
        if(milliseconds < 0) {
            return "";
        }

        return new SimpleDateFormat("h a", new Locale("en")).format(new Date(milliseconds));
    }
}
