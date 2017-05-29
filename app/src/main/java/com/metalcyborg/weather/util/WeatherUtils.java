package com.metalcyborg.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.metalcyborg.weather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherUtils {

    private static final String UNITS_H_PA = "hPa";
    private static final String UNITS_MM_HG = "mmHg";
    private static final String UNITS_M_SEC = "m/sec";
    private static final String UNITS_KM_H = "km/h";
    private static final String UNITS_MI_H = "mi/h";
    private static final String UNITS_HUMIDITY = "%";

    public enum TemperatureUnits {
        KELVIN,
        FAHRENHEIT,
        CELSIUS
    }

    public enum PressureUnits {
        H_PA,
        MM_HG
    }

    public enum SpeedUnits {
        M_SEC,
        KM_H,
        MI_H
    }

    public enum TimeUnits {
        CLOCK_12_H,
        CLOCK_24_H
    }

    public enum Wind {
        N,
        S,
        W,
        E,
        NW,
        NE,
        SW,
        SE
    }

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

    public static String convertLongToTimeString(long milliseconds, TimeUnits units) {
        if(milliseconds < 0) {
            return "";
        }

        if(units == TimeUnits.CLOCK_12_H) {
            return new SimpleDateFormat("h:mm a", new Locale("en")).format(new Date(milliseconds));
        } else {
            // 24 h
            return new SimpleDateFormat("H:mm", new Locale("en")).format(new Date(milliseconds));
        }

    }

    public static String convertLongToDurationString(long milliseconds) {
        // Without seconds
        if(milliseconds < 0)
            return "";

        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;

        String timeStr = "" + hours + ":";

        if(minutes < 10) {
            timeStr += "0";
        }

        timeStr += minutes;
//        timeStr += minutes + ":";
//
//        if(seconds < 10) {
//            timeStr += "0";
//        }
//
//        timeStr += seconds;

        return timeStr;
    }

    public static String getTemperatureString(float temp, TemperatureUnits units) {
        String tempStr;

        switch (units) {

            case KELVIN:
                tempStr = String.valueOf(Math.round(temp)) + "K";
                break;
            case FAHRENHEIT:
                tempStr = String.valueOf(Math.round(convertKelvinToFahrenheit(temp))) + (char)0x00B0;
                break;
            case CELSIUS:
                tempStr = String.valueOf(Math.round(convertKelvinToCelsius(temp))) + (char)0x00B0;
                break;
            default:
                tempStr = String.valueOf(Math.round(temp)) + "K";
        }

        if(units != TemperatureUnits.KELVIN) {
            if (temp < 0) {
                tempStr = "-" + tempStr;
            } else if (temp > 0) {
                tempStr = "+" + tempStr;
            }
        }

        return tempStr;
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

    public static int getWeatherImageId(String icon) {
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

    public static float convertKelvinToFahrenheit(float kelvin) {
        return kelvin * 9 / 5 - 459.67f;
    }

    public static float convertKelvinToCelsius(float kelvin) {
        return kelvin - 273.15f;
    }

    public static TemperatureUnits getCurrentTempUnits(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String tempUnitsStr = sharedPreferences
                .getString(context.getString(R.string.key_pref_temperature), "");
        WeatherUtils.TemperatureUnits tempUnits;
        try {
            tempUnits = WeatherUtils.TemperatureUnits.valueOf(tempUnitsStr);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            tempUnits = WeatherUtils.TemperatureUnits.CELSIUS;
        }

        return tempUnits;
    }

    public static PressureUnits getCurrentPressureUnits(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String pressureUnitsStr = sharedPreferences
                .getString(context.getString(R.string.key_pref_pressure), "");
        WeatherUtils.PressureUnits pressureUnits;
        try {
            pressureUnits = WeatherUtils.PressureUnits.valueOf(pressureUnitsStr);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            pressureUnits = PressureUnits.H_PA;
        }

        return pressureUnits;
    }

    public static SpeedUnits getCurrentSpeedUnits(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String speedUnitsStr = sharedPreferences
                .getString(context.getString(R.string.key_pref_speed), "");
        WeatherUtils.SpeedUnits speedUnits;
        try {
            speedUnits = WeatherUtils.SpeedUnits.valueOf(speedUnitsStr);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            speedUnits = SpeedUnits.M_SEC;
        }

        return speedUnits;
    }

    public static TimeUnits getCurrentTimeUnits(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String timeUnitsStr = sharedPreferences
                .getString(context.getString(R.string.key_pref_time), "");
        WeatherUtils.TimeUnits timeUnits;
        try {
            timeUnits = WeatherUtils.TimeUnits.valueOf(timeUnitsStr);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            timeUnits = TimeUnits.CLOCK_12_H;
        }

        return timeUnits;
    }

    private static float convertHPaToMmHg(float pressureHPa) {
        return pressureHPa * 0.75006375541921f;
    }

    private static float convertMetersSecToKilometersHour(float speedMetersSec) {
        return speedMetersSec * 3.6f;
    }

    private static float convertMetersSecToMilesHour(float speedMetersSec) {
        return speedMetersSec * 2.236936f;
    }

    public static String getPressureString(float pressure, PressureUnits units) {
        String pressureStr = "";
        if(units == PressureUnits.MM_HG) {
            pressureStr += Math.round(convertHPaToMmHg(pressure)) + " " + UNITS_MM_HG;
        } else {
            pressureStr += Math.round(pressure) + " " + UNITS_H_PA;
        }

        return pressureStr;
    }

    public static String getSpeedString(float speed, SpeedUnits units) {
        String speedStr = "";

        switch (units) {

            case M_SEC:
                speedStr += Math.round(speed) + " " + UNITS_M_SEC;
                break;
            case KM_H:
                speedStr += Math.round(convertMetersSecToKilometersHour(speed)) + " " + UNITS_KM_H;
                break;
            case MI_H:
                speedStr += Math.round(convertMetersSecToMilesHour(speed)) + " " + UNITS_MI_H;
                break;
            default:
                speedStr += Math.round(speed) + " " + UNITS_M_SEC;
        }

        return speedStr;
    }

    public static String getHumidityString(float humidity) {
        return "" + Math.round(humidity) + " " + UNITS_HUMIDITY;
    }

    public static Wind getWindDirectionByAngle(float angle) {
        if((angle >= 337.5 && angle < 360) || (angle >=0 && angle < 22.5)) {
            return Wind.N;
        } else if(angle >= 22.5 && angle < (45 + 22.5)) {
            return Wind.NE;
        } else if(angle >= (45 + 22.5) && angle < (90 + 22.5)) {
            return Wind.E;
        } else if(angle >= (90 + 22.5) && angle < (135 + 22.5)) {
            return Wind.SE;
        } else if(angle >= (135 + 22.5) && angle < (180 + 22.5)) {
            return Wind.S;
        } else if(angle >= (180 + 22.5) && angle < (225 + 22.5)) {
            return Wind.SW;
        } else if(angle >= (225 + 22.5) && angle < (270 + 22.5)) {
            return Wind.W;
        } else if(angle >= (270 + 22.5) && angle < (315 + 22.5)) {
            return Wind.NW;
        } else if(angle >= (315 + 22.5) && angle < 360) {
            return Wind.N;
        }

        return null;
    }
}