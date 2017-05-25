package com.metalcyborg.weather.data.source;

import com.google.common.collect.Lists;
import com.metalcyborg.weather.citylist.parseservice.CityData;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.local.LocalDataSource;
import com.metalcyborg.weather.data.source.remote.RemoteDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertThat;

/**
 * Created by metalcyborg on 18.04.17.
 */

public class WeatherRepositoryTest {

    private static final CityData CITY_DATA_0 = new CityData(0, "City_0", "Country_0",
            new CityData.Coord(10.0, 20.0));
    private static final CityData CITY_DATA_1 = new CityData(1, "City_1", "Country_1",
            new CityData.Coord(20.0, 30.0));
    private static final CityData[] CITY_DATA = new CityData[]{CITY_DATA_0, CITY_DATA_1};


    private static final String CITY_ID_1 = "1";
    private static final String CITY_ID_2 = "2";
    private static final String CITY_ID_3 = "3";
    private static final City CITY_1 = new City(CITY_ID_1, "City 1", "Country 1", 10, 20);
    private static final City CITY_2 = new City(CITY_ID_2, "City 2", "Country 2", 20, 40);
    private static final City CITY_3 = new City(CITY_ID_3, "City 3", "Country 3", 30, 60);
    private static final Weather WEATHER_1 = new Weather(100);
    private static final Weather WEATHER_2 = new Weather(200);
    private static final Weather WEATHER_3 = new Weather(300);
    private static final CityWeather CITY_WEATHER_1 = new CityWeather(CITY_1, WEATHER_1);
    private static final CityWeather CITY_WEATHER_2 = new CityWeather(CITY_2, null); // Empty weather data
    private static final CityWeather CITY_WEATHER_3 = new CityWeather(CITY_3, WEATHER_3);
    private static final List<CityWeather> CITY_WEATHER_LIST =
            Lists.newArrayList(CITY_WEATHER_1, CITY_WEATHER_2, CITY_WEATHER_3);
    private static final List<Weather> FORECAST = Lists.newArrayList(WEATHER_1, WEATHER_2);

    private WeatherRepository mWeatherRepository;

    @Mock
    private LocalDataSource mLocalDataSource;

    @Mock
    private RemoteDataSource mRemoteDataSource;

    @Mock
    private WeatherDataSource.FindCityListCallback mFindCityListCallback;

    @Mock
    private WeatherDataSource.LoadWeatherCallback mLoadWeatherCallback;

    @Mock
    private WeatherDataSource.LoadForecastCallback mLoad3HForecastCallback;

    @Mock
    private WeatherDataSource.LoadForecastCallback mLoad13DForecastCallback;

    @Captor
    private ArgumentCaptor<LocalDataSource.LoadWeatherListCallback> mLoadWeatherListCallbackCaptor;

    @Captor
    private ArgumentCaptor<RemoteDataSource.GetWeatherCallback> mGetWeatherCallbackCaptor;

    @Captor
    private ArgumentCaptor<LocalDataSource.LoadForecastCallback> mLoad3HForecastCallbackCaptor;

    @Captor
    private ArgumentCaptor<LocalDataSource.LoadForecastCallback> mLoad13DForecastCallbackCaptor;

    @Captor
    private ArgumentCaptor<RemoteDataSource.GetForecastCallback> mGet3HForecastCallbackCaptor;

    @Captor
    private ArgumentCaptor<RemoteDataSource.GetForecastCallback> mGet13DForecastCallbackCaptor;

    @Before
    public void setupWeatherRepository() {
        MockitoAnnotations.initMocks(this);

        mWeatherRepository = WeatherRepository.getInstance(mLocalDataSource, mRemoteDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        WeatherRepository.destroyInstance();
    }

    @Test
    public void isCitiesDataAdded() {
        mWeatherRepository.isCitiesDataAdded();

        verify(mLocalDataSource).isCitiesDataAdded();
    }

    @Test
    public void addCitiesData() {
        mWeatherRepository.addCitiesData(CITY_DATA);

        verify(mLocalDataSource).addCitiesData(CITY_DATA);
    }

    @Test
    public void findCitiesByPartOfTheName() {
        String partOfTheName = "Abc";
        int queryLimit = 20;
        mWeatherRepository.findCitiesByPartOfTheName(partOfTheName, queryLimit,
                mFindCityListCallback);

        verify(mLocalDataSource).findCitiesByPartOfTheName(eq(partOfTheName), eq(queryLimit),
                any(LocalDataSource.FindCityListCallback.class));
    }

    @Test
    public void addNewCityToWeatherList() {
        City city = new City("0", "City 0", "Country 0", 100, 200);
        mWeatherRepository.addNewCityToChosenCityList(city);

        verify(mLocalDataSource).addNewCityToChosenCityList(city);
        // Add to cache
        assertThat(mWeatherRepository.mCachedWeather.size(), is(1));
    }

    private void checkRemoteDataSource(List<CityWeather> cityWeatherList,
                                       WeatherDataSource.LoadWeatherCallback callback) {
        for(CityWeather cityWeather : cityWeatherList) {

            verify(mRemoteDataSource).loadWeatherData(eq(cityWeather.getCity().getOpenWeatherId()),
                    mGetWeatherCallbackCaptor.capture());

            // Data not available
            mGetWeatherCallbackCaptor.getValue()
                    .onDataNotAvailable(cityWeather.getCity().getOpenWeatherId());
            if(cityWeather.getWeather() == null) {
                // Show error if local data not available
                verify(callback).onDataNotAvailable(cityWeather.getCity().getOpenWeatherId());
            } else {
                // Show nothing if local data available
                verify(callback, never()).onDataNotAvailable(cityWeather.getCity().getOpenWeatherId());
            }

            // New data received
            mGetWeatherCallbackCaptor.getValue()
                    .onDataLoaded(cityWeather.getCity().getOpenWeatherId(),
                            WEATHER_1);
            verify(mLocalDataSource).updateCurrentWeather(cityWeather.getCity().getOpenWeatherId(),
                    WEATHER_1);
            verify(callback).onDataLoaded(cityWeather.getCity().getOpenWeatherId(),
                    WEATHER_1);
        }
    }

    private void fillCurrentWeatherCache() {
        mWeatherRepository.mCachedWeather = new LinkedHashMap<>();
        for(CityWeather cityWeather : CITY_WEATHER_LIST) {
            mWeatherRepository.mCachedWeather.put(cityWeather.getCity().getOpenWeatherId(),
                    cityWeather);
        }
    }

    @Test
    public void getWeatherList_loadFromCache() {
        fillCurrentWeatherCache();

        mWeatherRepository.loadWeatherData(mLoadWeatherCallback);
        assertThat(mWeatherRepository.mCachedWeather.size(), is(CITY_WEATHER_LIST.size()));
        verify(mLoadWeatherCallback).onDataListLoaded(CITY_WEATHER_LIST);

        checkRemoteDataSource(CITY_WEATHER_LIST, mLoadWeatherCallback);
    }

    @Test
    public void getWeatherList_loadFromLocalData() {
        mWeatherRepository.loadWeatherData(mLoadWeatherCallback);

        verify(mLocalDataSource).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
        mLoadWeatherListCallbackCaptor.getValue().onDataLoaded(CITY_WEATHER_LIST);
        verify(mLoadWeatherCallback).onDataListLoaded(CITY_WEATHER_LIST);
        assertThat(mWeatherRepository.mCachedWeather.size(), is(CITY_WEATHER_LIST.size()));

        checkRemoteDataSource(CITY_WEATHER_LIST, mLoadWeatherCallback);
    }

    private void fillForecastCache() {
        mWeatherRepository.mCached3hForecast = new LinkedHashMap<>();
        mWeatherRepository.mCached3hForecast.put(CITY_ID_1, FORECAST);

        mWeatherRepository.mCached13DForecast = new LinkedHashMap<>();
        mWeatherRepository.mCached13DForecast.put(CITY_ID_1, FORECAST);
    }

    @Test
    public void getForecast_loadFromCache() {
        fillForecastCache();

        // 3h forecast
        mWeatherRepository.load3HForecastData(CITY_ID_1, mLoad3HForecastCallback);
        verify(mLoad3HForecastCallback).onDataLoaded(FORECAST);

        // 13d forecast
        mWeatherRepository.load13DForecastData(CITY_ID_1, mLoad13DForecastCallback);
        verify(mLoad13DForecastCallback).onDataLoaded(FORECAST);
    }

    @Test
    public void getForecast_loadFromLocalSource() {
        // 3h forecast
        mWeatherRepository.load3HForecastData(CITY_ID_1, mLoad3HForecastCallback);

        verify(mLocalDataSource).load3HForecastData(eq(CITY_ID_1), mLoad3HForecastCallbackCaptor.capture());
        mLoad3HForecastCallbackCaptor.getValue().onDataLoaded(FORECAST);
        verify(mLoad3HForecastCallback).onDataLoaded(FORECAST);
        assertThat(mWeatherRepository.mCached3hForecast.get(CITY_ID_1).size(), is(FORECAST.size()));

        // 13d forecast
        mWeatherRepository.load13DForecastData(CITY_ID_1, mLoad13DForecastCallback);

        verify(mLocalDataSource).load13DForecastData(eq(CITY_ID_1), mLoad13DForecastCallbackCaptor.capture());
        mLoad13DForecastCallbackCaptor.getValue().onDataLoaded(FORECAST);
        verify(mLoad13DForecastCallback).onDataLoaded(FORECAST);
        assertThat(mWeatherRepository.mCached13DForecast.get(CITY_ID_1).size(), is(FORECAST.size()));
    }

    @Test
    public void getForecast_loadFromRemoteSource() {
        // 3h forecast
        // Local data was already loaded
        mWeatherRepository.load3HForecastData(CITY_ID_1, mLoad3HForecastCallback);

        verify(mRemoteDataSource).load3HForecastData(eq(CITY_ID_1), mGet3HForecastCallbackCaptor.capture());
        mGet3HForecastCallbackCaptor.getValue().onDataLoaded(FORECAST);
        verify(mLocalDataSource).update3HForecast(CITY_ID_1, FORECAST);
        assertThat(mWeatherRepository.mCached3hForecast.get(CITY_ID_1).size(), is(FORECAST.size()));
        verify(mLoad3HForecastCallback).onDataLoaded(FORECAST);

        // 13d forecast
        // Local data was already loaded
        mWeatherRepository.load13DForecastData(CITY_ID_1, mLoad13DForecastCallback);

        verify(mRemoteDataSource).load13DForecastData(eq(CITY_ID_1), mGet13DForecastCallbackCaptor.capture());
        mGet13DForecastCallbackCaptor.getValue().onDataLoaded(FORECAST);
        verify(mLocalDataSource).update13DForecast(CITY_ID_1, FORECAST);
        assertThat(mWeatherRepository.mCached13DForecast.get(CITY_ID_1).size(), is(FORECAST.size()));
        verify(mLoad13DForecastCallback).onDataLoaded(FORECAST);
    }

    @Test
    public void getForecast_dataNotAvailable() {
        // 3h forecast
        // Forecast cache is empty
        mWeatherRepository.load3HForecastData(CITY_ID_1, mLoad3HForecastCallback);

        verify(mRemoteDataSource).load3HForecastData(eq(CITY_ID_1), mGet3HForecastCallbackCaptor.capture());
        mGet3HForecastCallbackCaptor.getValue().onDataNotAvailable();
        verify(mLoad3HForecastCallback).onDataNotAvailable();

        // 13d forecast
        // Forecast cache is empty
        mWeatherRepository.load13DForecastData(CITY_ID_1, mLoad13DForecastCallback);

        verify(mRemoteDataSource).load13DForecastData(eq(CITY_ID_1), mGet13DForecastCallbackCaptor.capture());
        mGet13DForecastCallbackCaptor.getValue().onDataNotAvailable();
        verify(mLoad13DForecastCallback).onDataNotAvailable();
    }

    @Test
    public void getForecast_onlyRemoteDataNotAvailable() {
        // Forecast cache is not empty
        fillForecastCache();

        // 3h forecast
        mWeatherRepository.load3HForecastData(CITY_ID_1, mLoad3HForecastCallback);

        verify(mRemoteDataSource).load3HForecastData(eq(CITY_ID_1), mGet3HForecastCallbackCaptor.capture());
        mGet3HForecastCallbackCaptor.getValue().onDataNotAvailable();
        // Do nothing
        verify(mLoad3HForecastCallback, never()).onDataNotAvailable();

        // 13d forecast
        mWeatherRepository.load13DForecastData(CITY_ID_1, mLoad13DForecastCallback);

        verify(mRemoteDataSource).load13DForecastData(eq(CITY_ID_1), mGet13DForecastCallbackCaptor.capture());
        mGet13DForecastCallbackCaptor.getValue().onDataNotAvailable();
        // Do nothing
        verify(mLoad13DForecastCallback, never()).onDataNotAvailable();
    }

    @Test
    public void deleteCitiesFromChosenCityList() {
        List<CityWeather> deletedCityList = Lists.newArrayList(CITY_WEATHER_1, CITY_WEATHER_2);
        fillCurrentWeatherCache();
        assertThat(mWeatherRepository.mCachedWeather.size(), is(CITY_WEATHER_LIST.size()));

        mWeatherRepository.deleteCitiesFromChosenCityList(deletedCityList);
        // Delete from local db
        verify(mLocalDataSource).deleteCitiesFromChosenCityList(deletedCityList);

        // Delete from cache
        assertThat(mWeatherRepository.mCachedWeather.size(),
                is(CITY_WEATHER_LIST.size() - deletedCityList.size()));
    }
}
