package com.metalcyborg.weather.detail;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.TestUtils;
import com.metalcyborg.weather.data.FakeWeatherRemoteDataSource;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;
import com.metalcyborg.weather.util.WeatherUtils;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

public class DetailScreenTest {

    private static final String TEST_CITY_ID = "100";
    private static final String TEST_CITY_NAME = "City1";

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule =
            new ActivityTestRule<>(DetailActivity.class, true, false);

    private void launchActivity(String cityId, String cityName, WeatherDetails details) {
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.EXTRAS_WEATHER_DETAILS, details);
        intent.putExtra(DetailActivity.EXTRAS_CITY_ID, cityId);
        intent.putExtra(DetailActivity.EXTRAS_CITY_NAME, cityName);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void displayCurrentWeatherAndForecastData() {
        // Add data to the fake remote data source
        FakeWeatherRemoteDataSource remoteDataSource = FakeWeatherRemoteDataSource.getInstance();
        // 3 hour forecast
        List<Weather> forecast3H = WeatherUtils.generateWeatherList(6);
        remoteDataSource.addForecast3H(TEST_CITY_ID, forecast3H);
        // Daily forecast
        List<Weather> forecastDaily = WeatherUtils.generateWeatherList(3);
        remoteDataSource.addForecastDaily(TEST_CITY_ID, forecastDaily);

        // Launch DetailActivity with extras
        WeatherDetails details = WeatherUtils.generateTestWeatherDetailsData();
        launchActivity(TEST_CITY_ID, TEST_CITY_NAME, details);

        // Check toolbar
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(TEST_CITY_NAME)));
        onView(withId(R.id.header_temp)).check(matches(withText(
                WeatherUtils.getTemperatureString(details.getTemperature(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())))));

        // Check current weather details
        checkCurrentWeatherData(details, 1);

        // Check 3h forecast
        check3HForecast(3, forecast3H);

        // Check daily forecast, positions 5 and 6
        checkDailyForecast(5, true, forecastDaily);
    }

    private void checkDailyForecast(int startAdapterPosition, boolean ignoreFirstItem,
                                    List<Weather> forecastDaily) {
        for (int i = 0; i < forecastDaily.size(); ++i) {
            if (i == 0 && ignoreFirstItem)
                continue;

            onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                    .atPosition(startAdapterPosition + i - 1))
                    .check(matches(allOf(
                            hasDescendant(withText(WeatherUtils.convertLongToDateString(forecastDaily.get(i).getDateTime() * 1000))),
                            hasDescendant(withText(WeatherUtils.convertLongToDayString(forecastDaily.get(i).getDateTime() * 1000))),
                            hasDescendant(withText(WeatherUtils.getTemperatureString(forecastDaily.get(i).getMain().getDayTemp(),
                                    WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())))),
                            hasDescendant(withText(WeatherUtils.getTemperatureString(forecastDaily.get(i).getMain().getNightTemp(),
                                    WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext()))))
                    )));
        }
    }

    private void checkCurrentWeatherData(WeatherDetails details, int adapterPosition) {
        // Pressure
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.pressure))
                .check(matches(withText(WeatherUtils.getPressureString(details.getPressure(),
                        WeatherUtils.getCurrentPressureUnits(InstrumentationRegistry.getTargetContext())))));
        // Humidity
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.humidity))
                .check(matches(withText(WeatherUtils.getHumidityString(details.getHumidity()))));
        // Wind
        // Speed
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.windSpeed))
                .check(matches(withText(WeatherUtils.getSpeedString(details.getWindSpeed(),
                        WeatherUtils.getCurrentSpeedUnits(InstrumentationRegistry.getTargetContext())))));

        // Sunrise/sunset
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.sunrise))
                .check(matches(withText(WeatherUtils.convertLongToTimeString(details.getSunrise() * 1000,
                        WeatherUtils.getCurrentTimeUnits(InstrumentationRegistry.getTargetContext())))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.sunset))
                .check(matches(withText(WeatherUtils.convertLongToTimeString(details.getSunset() * 1000,
                        WeatherUtils.getCurrentTimeUnits(InstrumentationRegistry.getTargetContext())))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.dayLightTime))
                .check(matches(withText(WeatherUtils.convertLongToDurationString(
                        (details.getSunset() - details.getSunrise()) * 1000))));
    }

    private void check3HForecast(int adapterPosition, List<Weather> forecast) {
        // 6 forecast items
        // Time textViews
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.time1))
                .check(matches(withText(WeatherUtils.convertLongToTimeString(
                        forecast.get(0).getDateTime() * 1000,
                        WeatherUtils.getCurrentTimeUnits(InstrumentationRegistry.getTargetContext())))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.time2))
                .check(matches(withText(WeatherUtils.convertLongToTimeString(
                        forecast.get(1).getDateTime() * 1000,
                        WeatherUtils.getCurrentTimeUnits(InstrumentationRegistry.getTargetContext())))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.time3))
                .check(matches(withText(WeatherUtils.convertLongToTimeString(
                        forecast.get(2).getDateTime() * 1000,
                        WeatherUtils.getCurrentTimeUnits(InstrumentationRegistry.getTargetContext())))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.time4))
                .check(matches(withText(WeatherUtils.convertLongToTimeString(
                        forecast.get(3).getDateTime() * 1000,
                        WeatherUtils.getCurrentTimeUnits(InstrumentationRegistry.getTargetContext())))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.time5))
                .check(matches(withText(WeatherUtils.convertLongToTimeString(
                        forecast.get(4).getDateTime() * 1000,
                        WeatherUtils.getCurrentTimeUnits(InstrumentationRegistry.getTargetContext())))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.time6))
                .check(matches(withText(WeatherUtils.convertLongToTimeString(
                        forecast.get(5).getDateTime() * 1000,
                        WeatherUtils.getCurrentTimeUnits(InstrumentationRegistry.getTargetContext())))));
        // Temperature textViews
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.temp1))
                .check(matches(withText(WeatherUtils.getTemperatureString(
                        forecast.get(0).getMain().getDayTemp(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.temp2))
                .check(matches(withText(WeatherUtils.getTemperatureString(
                        forecast.get(1).getMain().getDayTemp(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.temp3))
                .check(matches(withText(WeatherUtils.getTemperatureString(
                        forecast.get(2).getMain().getDayTemp(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.temp4))
                .check(matches(withText(WeatherUtils.getTemperatureString(
                        forecast.get(3).getMain().getDayTemp(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.temp5))
                .check(matches(withText(WeatherUtils.getTemperatureString(
                        forecast.get(4).getMain().getDayTemp(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.temp6))
                .check(matches(withText(WeatherUtils.getTemperatureString(
                        forecast.get(5).getMain().getDayTemp(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())
                ))));
        // Weather imageViews
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.image1))
                .check(matches(TestUtils.withImage(WeatherUtils.getIconId(
                        forecast.get(0).getWeatherDescription().getIcon()
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.image2))
                .check(matches(TestUtils.withImage(WeatherUtils.getIconId(
                        forecast.get(1).getWeatherDescription().getIcon()
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.image3))
                .check(matches(TestUtils.withImage(WeatherUtils.getIconId(
                        forecast.get(2).getWeatherDescription().getIcon()
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.image4))
                .check(matches(TestUtils.withImage(WeatherUtils.getIconId(
                        forecast.get(3).getWeatherDescription().getIcon()
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.image5))
                .check(matches(TestUtils.withImage(WeatherUtils.getIconId(
                        forecast.get(4).getWeatherDescription().getIcon()
                ))));
        onView(TestUtils.withRecyclerView(R.id.forecastRecycler)
                .atPositionOnView(adapterPosition, R.id.image6))
                .check(matches(TestUtils.withImage(WeatherUtils.getIconId(
                        forecast.get(5).getWeatherDescription().getIcon()
                ))));
    }
}
