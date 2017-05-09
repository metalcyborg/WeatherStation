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

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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


    private static final City CITY_1 = new City("0", "1", "City 1", "Country 1", 10, 20);
    private static final City CITY_2 = new City("1", "2", "City 2", "Country 2", 10, 20);
    private static final Weather WEATHER_1 = new Weather("0");
    private static final Weather WEATHER_2 = new Weather("1");
    private static final CityWeather CITY_WEATHER_1 = new CityWeather(CITY_1, WEATHER_1);
    private static final CityWeather CITY_WEATHER_2 = new CityWeather(CITY_2, WEATHER_2);
    private static final List<String> CITY_ID_LIST = Lists.newArrayList("1", "2");
    private static final List<CityWeather> WEATHER_LIST = Lists.newArrayList(CITY_WEATHER_1,
            CITY_WEATHER_2);

    private WeatherRepository mWeatherRepository;

    @Mock
    private LocalDataSource mLocalDataSource;

    @Mock
    private RemoteDataSource mRemoteDataSource;

    @Mock
    private WeatherDataSource.LoadCityDataCallback mLoadCityDataCallback;

    @Mock
    private WeatherDataSource.FindCityListCallback mFindCityListCallback;

    @Mock
    private WeatherDataSource.LoadWeatherCallback mLoadWeatherListCallback;

    @Captor
    private ArgumentCaptor<LocalDataSource.LoadWeatherListCallback> mLoadWeatherListCallbackCaptor;

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
        City city = new City("0", "0", "City 0", "Country 0", 100, 200);
        mWeatherRepository.addNewCityToChosenCityList(city);

        verify(mLocalDataSource).addNewCityToChosenCityList(city);
    }

    @Test
    public void getWeatherList_loadFromCache() {
//        mWeatherRepository.mCachedWeather = WEATHER_LIST;

        mWeatherRepository.loadWeatherData(mLoadWeatherListCallback);
        assertThat(mWeatherRepository.mCachedWeather.size(), is(2));
        verify(mLoadWeatherListCallback).onDataListLoaded(WEATHER_LIST);
    }

    @Test
    public void getWeatherList_loadFromLocalData() {
        mWeatherRepository.loadWeatherData(mLoadWeatherListCallback);

        verify(mLocalDataSource).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
        mLoadWeatherListCallbackCaptor.getValue().onDataLoaded(WEATHER_LIST);
        verify(mLoadWeatherListCallback).onDataListLoaded(WEATHER_LIST);
        assertThat(mWeatherRepository.mCachedWeather.size(), is(2));
    }

//    @Test
//    public void getWeatherList_loadFromRemoteServer() {
//        // First call. Cache == null
//        mWeatherRepository.loadWeatherData(mLoadWeatherListCallback);
//
//        verify(mLocalDataSource).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
//        // Local data not found
//        mLoadWeatherListCallbackCaptor.getValue().onDataListNotAvailable();
//
//        verify(mRemoteDataSource).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
//        mLoadWeatherListCallbackCaptor.getValue().onDataListLoaded(WEATHER_LIST);
//        // Add data to cache and local db
//        assertThat(mWeatherRepository.mCachedWeather.size(), is(2));
//        verify(mLocalDataSource).addCurrentWeatherData(WEATHER_LIST);
//    }

//    @Test
//    public void getWeatherList_dataNotAvailable() {
//        // First call. Cache == null
//        mWeatherRepository.loadWeatherData(mLoadWeatherListCallback);
//
//        verify(mLocalDataSource).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
//        // Local data not found
//        mLoadWeatherListCallbackCaptor.getValue().onDataListNotAvailable();
//        verify(mRemoteDataSource).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
//        mLoadWeatherListCallbackCaptor.getValue().onDataListNotAvailable();
//        verify(mLoadWeatherListCallback).onDataListNotAvailable();
//    }
}
