package com.metalcyborg.weather.citylist;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.TestUtils;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.FakeWeatherRemoteDataSource;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.WeatherRepository;
import com.metalcyborg.weather.data.source.local.LocalDataSource;
import com.metalcyborg.weather.data.source.local.WeatherLocalDataSource;
import com.metalcyborg.weather.util.WeatherUtils;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class CityListScreenTestMock {

    private static final String TEST_CITY_ID = "0";
    private static final String TEST_CITY_NAME = "Tyumen";
    private static final String TEST_COUNTRY_NAME = "RU";
    private static final String TEST_CITY_ID_2 = "1";
    private static final String TEST_CITY_NAME_2 = "Moscow";
    private static final String TEST_COUNTRY_NAME_2 = "RU";
    private static final Weather TEST_WEATHER = WeatherUtils.generateTestWeatherData();
    private static final Weather TEST_WEATHER_2 = WeatherUtils.generateTestWeatherData();

    @Rule
    public ActivityTestRule<CityListActivity> mCityListActivityTestRule =
            new ActivityTestRule<>(CityListActivity.class, true, false);


    @Test
    public void displayCitiesWithCurrentWeatherData() {
        addCitiesDataToTheDataSources();
        // Launch activity
        Intent intent = new Intent();
        mCityListActivityTestRule.launchActivity(intent);

        onView(withId(R.id.recycler)).check(TestUtils.hasItemCount(2));
        onView(TestUtils.withRecyclerView(R.id.recycler).atPosition(0)).check(matches(allOf(
                hasDescendant(withText(TEST_CITY_NAME)),
                hasDescendant(withText(WeatherUtils.getTemperatureString(TEST_WEATHER.getMain().getDayTemp(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())))),
                hasDescendant(TestUtils.withImage(
                        WeatherUtils.getIconId(TEST_WEATHER.getWeatherDescription().getIcon())))
                )));
        onView(TestUtils.withRecyclerView(R.id.recycler).atPosition(1)).check(matches(allOf(
                hasDescendant(withText(TEST_CITY_NAME_2)),
                hasDescendant(withText(WeatherUtils.getTemperatureString(TEST_WEATHER_2.getMain().getDayTemp(),
                        WeatherUtils.getCurrentTempUnits(InstrumentationRegistry.getTargetContext())))),
                hasDescendant(TestUtils.withImage(
                        WeatherUtils.getIconId(TEST_WEATHER_2.getWeatherDescription().getIcon())))
        )));
    }

    private void addCitiesDataToTheDataSources() {
        // Delete all cities
        WeatherRepository repository = Injection
                .provideWeatherRepository(InstrumentationRegistry.getTargetContext());
        repository.deleteAllDataFromCityAndWeatherLists();

        // Add city and weather data
        LocalDataSource mLocalDataSource = WeatherLocalDataSource
                .getInstance(InstrumentationRegistry.getTargetContext());
        City city1 = new City(TEST_CITY_ID, TEST_CITY_NAME, TEST_COUNTRY_NAME);
        City city2 = new City(TEST_CITY_ID_2, TEST_CITY_NAME_2, TEST_COUNTRY_NAME_2);
        mLocalDataSource.addNewCityToChosenCityList(city1);
        mLocalDataSource.addNewCityToChosenCityList(city2);

        FakeWeatherRemoteDataSource fakeRemoteDataSource = FakeWeatherRemoteDataSource.getInstance();
        fakeRemoteDataSource.addCurrentWeatherData(city1.getOpenWeatherId(), TEST_WEATHER);
        fakeRemoteDataSource.addCurrentWeatherData(city2.getOpenWeatherId(), TEST_WEATHER_2);
    }
}
