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

        return new SimpleDateFormat("MMM d", new Locale("en")).format(new Date(milliseconds));
    }

    public static String convertLongToDayString(long milliseconds) {
        if(milliseconds < 0) {
            return "";
        }

        return new SimpleDateFormat("EEE", new Locale("en")).format(new Date(milliseconds));
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

    public static int getIconId(String icon) {
        switch (icon) {
            case "01d":
                return R.drawable.ic_weather_sunny;
            case "01n":
                return R.drawable.ic_weather_night;
            case "02d":
            case "02n":
                return R.drawable.ic_weather_partlycloudy;
            case "03d":
            case "03n":
                return R.drawable.ic_weather_cloudy;
            case "04d":
            case "04n":
                return R.drawable.ic_weather_cloudy;
            case "09d":
            case "09n":
                return R.drawable.ic_weather_pouring;
            case "10d":
            case "10n":
                return R.drawable.ic_weather_rainy;
            case "11d":
            case "11n":
                return R.drawable.ic_weather_lightning;
            case "13d":
            case "13n":
                return R.drawable.ic_weather_hail;
            case "50d":
            case "50n":
                return R.drawable.ic_weather_fog;
        }

        return -1;
    }
}
