package com.metalcyborg.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.metalcyborg.weather.settings.SettingsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public enum TemperatureUnits {
        KELVIN,
        FAHRENHEIT,
        CELSIUS
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
                .getString(SettingsActivity.PREF_KEY_TEMP_UNITS, "");
        Utils.TemperatureUnits tempUnits;
        try {
            tempUnits = Utils.TemperatureUnits.valueOf(tempUnitsStr);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            tempUnits = Utils.TemperatureUnits.CELSIUS;
        }

        return tempUnits;
    }
}
