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


    private static final String CITY_ID = "1";
    private static final City CITY = new City(CITY_ID, "City 1", "Country 1", 10, 20);
    private static final Weather WEATHER = new Weather(100);
    private static final CityWeather CITY_WEATHER = new CityWeather(CITY, WEATHER);
    private static final List<CityWeather> WEATHER_LIST = Lists.newArrayList(CITY_WEATHER);

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
    private WeatherDataSource.LoadWeatherCallback mLoadWeatherCallback;

    @Captor
    private ArgumentCaptor<LocalDataSource.LoadWeatherListCallback> mLoadWeatherListCallbackCaptor;

    @Captor
    private ArgumentCaptor<RemoteDataSource.GetWeatherCallback> mGetWeatherCallbackCaptor;

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

    private void checkRemoteDataSource(String cityId,
                                       WeatherDataSource.LoadWeatherCallback callback) {
        // Data loaded
        verify(mRemoteDataSource).loadWeatherData(eq(cityId), mGetWeatherCallbackCaptor.capture());
        mGetWeatherCallbackCaptor.getValue().onDataLoaded(cityId, WEATHER);
        verify(mLocalDataSource).updateWeather(cityId, WEATHER);
        verify(callback).onDataLoaded(cityId, WEATHER);

        // Data not available
        mGetWeatherCallbackCaptor.getValue().onDataNotAvailable(cityId);
        verify((callback)).onDataNotAvailable(cityId);
    }

    @Test
    public void getWeatherList_loadFromCache() {
        mWeatherRepository.mCachedWeather = new LinkedHashMap<>();
        mWeatherRepository.mCachedWeather.put(CITY_ID, CITY_WEATHER);

        mWeatherRepository.loadWeatherData(mLoadWeatherCallback);
        assertThat(mWeatherRepository.mCachedWeather.size(), is(WEATHER_LIST.size()));
        verify(mLoadWeatherCallback).onDataListLoaded(WEATHER_LIST);

        checkRemoteDataSource(CITY_ID, mLoadWeatherCallback);
    }

    @Test
    public void getWeatherList_loadFromLocalData() {
        mWeatherRepository.loadWeatherData(mLoadWeatherCallback);

        verify(mLocalDataSource).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
        mLoadWeatherListCallbackCaptor.getValue().onDataLoaded(WEATHER_LIST);
        verify(mLoadWeatherCallback).onDataListLoaded(WEATHER_LIST);
        assertThat(mWeatherRepository.mCachedWeather.size(), is(WEATHER_LIST.size()));

        checkRemoteDataSource(CITY_ID, mLoadWeatherCallback);
    }
}
