package com.metalcyborg.weather.data;


import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.collect.Lists;

import com.metalcyborg.weather.data.source.local.LocalDataSource;
import com.metalcyborg.weather.data.source.local.WeatherLocalDataSource;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.metalcyborg.weather.util.WeatherUtils.generateTestWeatherData;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class LocalDataSourceTest {

    private static final City TEST_CITY = new City("0", "City 0", "Country 0", 10f, 20f,
            "TimeZone1");
    private static final City TEST_CITY_2 = new City("1", "City 1", "Country 1", 30f, 40f,
            "TimeZone2");

    private LocalDataSource mLocalDataSource;

    @Before
    public void setup() {
        mLocalDataSource = WeatherLocalDataSource
                .getInstance(InstrumentationRegistry.getTargetContext());
        mLocalDataSource.deleteAllCitiesAndForecastData();
    }

    @Test
    public void addNewCityToChosenCityList_retrieveCityList() {
        // Add new city to the chosen city list. Weather data is null
        mLocalDataSource.addNewCityToChosenCityList(TEST_CITY);
        // Then city info with null weather object can be retrieved from the database
        mLocalDataSource.loadWeatherData(new LocalDataSource.LoadWeatherListCallback() {
            @Override
            public void onDataLoaded(List<CityWeather> weatherData) {
                assertThat(weatherData.size(), is(1));
                assertThat(weatherData.get(0).getCity(), is(TEST_CITY));
                assertNull(weatherData.get(0).getWeather());
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }

    @Test
    public void updateCurrentWeatherData_retrieveCurrentWeatherData() {
        final Weather testWeather = generateTestWeatherData();

        // Add new city to the chosen city list. Weather data is null
        mLocalDataSource.addNewCityToChosenCityList(TEST_CITY);
        // Update weather data
        mLocalDataSource.updateCurrentWeather(TEST_CITY.getOpenWeatherId(),
                testWeather);
        // Then current weather info can be retrieved from database
        mLocalDataSource.loadWeatherData(new LocalDataSource.LoadWeatherListCallback() {
            @Override
            public void onDataLoaded(List<CityWeather> weatherData) {
                assertThat(weatherData.size(), is(1));
                assertThat(weatherData.get(0).getCity(), is(TEST_CITY));
                assertThat(weatherData.get(0).getWeather(), is(testWeather));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }

    @Test
    public void add3HForecastData_retrieve3HForecast() {
        final List<Weather> forecast3H = new ArrayList<>();
        forecast3H.add(generateTestWeatherData());
        forecast3H.add(generateTestWeatherData());

        final Weather currentWeather = generateTestWeatherData();

        // Add new city to the chosen city list. Weather data is null
        mLocalDataSource.addNewCityToChosenCityList(TEST_CITY);
        // Add current weather data
        mLocalDataSource.updateCurrentWeather(TEST_CITY.getOpenWeatherId(),
                currentWeather);
        // Update 3 hour forecast data
        mLocalDataSource.update3HForecast(TEST_CITY.getOpenWeatherId(),
                forecast3H);
        // Then 3 hour forecast can be retrieved
        mLocalDataSource.load3HForecastData(TEST_CITY.getOpenWeatherId(),
                new LocalDataSource.LoadForecastCallback() {
                    @Override
                    public void onDataLoaded(List<Weather> forecast) {
                        assertThat(forecast, is(forecast3H));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        fail("Callback error");
                    }
                });
    }

    @Test
    public void addDailyForecastData_retrieveDailyForecast() {
        final List<Weather> dailyForecast = new ArrayList<>();
        dailyForecast.add(generateTestWeatherData());
        dailyForecast.add(generateTestWeatherData());

        final Weather currentWeather = generateTestWeatherData();

        // Add new city to the chosen city list. Weather data is null
        mLocalDataSource.addNewCityToChosenCityList(TEST_CITY);
        // Add current weather data
        mLocalDataSource.updateCurrentWeather(TEST_CITY.getOpenWeatherId(),
                currentWeather);
        // Update daily forecast data
        mLocalDataSource.update13DForecast(TEST_CITY.getOpenWeatherId(),
                dailyForecast);
        // Then daily forecast can be retrieved
        mLocalDataSource.load13DForecastData(TEST_CITY.getOpenWeatherId(),
                new LocalDataSource.LoadForecastCallback() {
                    @Override
                    public void onDataLoaded(List<Weather> forecast) {
                        assertThat(forecast, is(dailyForecast));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        fail("Callback error");
                    }
                });
    }

    @Test
    public void addCitiesWithCurrentWeatherAndForecast_deleteCity() {
        LocalDataSource.GetWeatherCallback getWeatherCallback =
                mock(LocalDataSource.GetWeatherCallback.class);
        LocalDataSource.LoadForecastCallback load3HForecastCallback =
                mock(LocalDataSource.LoadForecastCallback.class);
        LocalDataSource.LoadForecastCallback load13DForecastCallback =
                mock(LocalDataSource.LoadForecastCallback.class);

        // Add two test cities
        addCurrentWeatherAndForecastData(TEST_CITY);
        addCurrentWeatherAndForecastData(TEST_CITY_2);
        // Delete first city data
        mLocalDataSource.deleteCitiesFromChosenCityList(Lists.newArrayList(TEST_CITY));
        // Verify that only one city data can be received
        mLocalDataSource.loadWeatherData(new LocalDataSource.LoadWeatherListCallback() {
            @Override
            public void onDataLoaded(List<CityWeather> weatherData) {
                assertThat(weatherData.size(), is(1));
                assertThat(weatherData.get(0).getCity(), is(TEST_CITY_2));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });

        mLocalDataSource.getWeatherByCityId(TEST_CITY.getOpenWeatherId(), getWeatherCallback);
        verify(getWeatherCallback).onDataNotAvailable();
        verify(getWeatherCallback, never()).onDataLoaded(any(Weather.class));

        mLocalDataSource.load3HForecastData(TEST_CITY.getOpenWeatherId(), load3HForecastCallback);
        verify(load3HForecastCallback).onDataNotAvailable();
        verify(load3HForecastCallback, never()).onDataLoaded(anyListOf(Weather.class));

        mLocalDataSource.load13DForecastData(TEST_CITY.getOpenWeatherId(), load13DForecastCallback);
        verify(load13DForecastCallback).onDataNotAvailable();
        verify(load13DForecastCallback, never()).onDataLoaded(anyListOf(Weather.class));
    }

    @Test
    public void addCity_deleteAllCities() {
        LocalDataSource.LoadWeatherListCallback loadWeatherListCallback =
                mock(LocalDataSource.LoadWeatherListCallback.class);
        LocalDataSource.GetWeatherCallback getWeatherCallback =
                mock(LocalDataSource.GetWeatherCallback.class);
        LocalDataSource.LoadForecastCallback load3HForecastCallback =
                mock(LocalDataSource.LoadForecastCallback.class);
        LocalDataSource.LoadForecastCallback load13DForecastCallback =
                mock(LocalDataSource.LoadForecastCallback.class);

        // Add test city
        addCurrentWeatherAndForecastData(TEST_CITY);
        // Delete all cities
        mLocalDataSource.deleteAllCitiesAndForecastData();

        // Verify that all data was deleted
        mLocalDataSource.loadWeatherData(loadWeatherListCallback);
        verify(loadWeatherListCallback).onDataNotAvailable();
        verify(loadWeatherListCallback, never()).onDataLoaded(anyListOf(CityWeather.class));

        mLocalDataSource.getWeatherByCityId(TEST_CITY.getOpenWeatherId(), getWeatherCallback);
        verify(getWeatherCallback).onDataNotAvailable();
        verify(getWeatherCallback, never()).onDataLoaded(any(Weather.class));

        mLocalDataSource.load3HForecastData(TEST_CITY.getOpenWeatherId(), load3HForecastCallback);
        verify(load3HForecastCallback).onDataNotAvailable();
        verify(load3HForecastCallback, never()).onDataLoaded(anyListOf(Weather.class));

        mLocalDataSource.load13DForecastData(TEST_CITY.getOpenWeatherId(), load13DForecastCallback);
        verify(load13DForecastCallback).onDataNotAvailable();
        verify(load13DForecastCallback, never()).onDataLoaded(anyListOf(Weather.class));
    }

    private void addCurrentWeatherAndForecastData(City city) {
        // Generate test data
        final Weather currentWeather = generateTestWeatherData();

        final List<Weather> dailyForecast = new ArrayList<>();
        dailyForecast.add(generateTestWeatherData());
        dailyForecast.add(generateTestWeatherData());

        final List<Weather> forecast3H = new ArrayList<>();
        forecast3H.add(generateTestWeatherData());
        forecast3H.add(generateTestWeatherData());
        forecast3H.add(generateTestWeatherData());

        // Add new city
        mLocalDataSource.addNewCityToChosenCityList(city);
        // Update current weather and forecast data
        mLocalDataSource.updateCurrentWeather(city.getOpenWeatherId(), currentWeather);
        mLocalDataSource.update3HForecast(city.getOpenWeatherId(), forecast3H);
        mLocalDataSource.update13DForecast(city.getOpenWeatherId(), dailyForecast);
    }
}
