package com.metalcyborg.weather.data.source;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.local.WeatherLocalDataSource;
import com.metalcyborg.weather.data.source.remote.WeatherRemoteDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by metalcyborg on 18.04.17.
 */

public class WeatherRepositoryTest {

    private WeatherRepository mWeatherRepository;

    @Mock
    private WeatherLocalDataSource mLocalDataSource;

    @Mock
    private WeatherRemoteDataSource mRemoteDataSource;

    @Mock
    private WeatherDataSource.LoadCityDataCallback mLoadCityDataCallback;

    @Mock
    private WeatherDataSource.FindCityListCallback mFindCityListCallback;

    @Before
    public void setupWeatherRepository() {
        MockitoAnnotations.initMocks(this);

        mWeatherRepository = WeatherRepository.getInstance(mLocalDataSource, mRemoteDataSource);
    }

    @Test
    public void isCitiesDataAdded() {
        mWeatherRepository.isCitiesDataAdded();

        verify(mLocalDataSource).isCitiesDataAdded();
    }

    @Test
    public void addCitiesData() {
        mWeatherRepository.addCitiesData(mLoadCityDataCallback);

        verify(mLocalDataSource).addCitiesData(any(WeatherDataSource.LoadCityDataCallback.class));
    }

    @Test
    public void findCitiesByPartOfTheName() {
        String partOfTheName = "Abc";
        mWeatherRepository.findCitiesByPartOfTheName(partOfTheName, mFindCityListCallback);

        verify(mLocalDataSource).findCitiesByPartOfTheName(eq(partOfTheName),
                any(WeatherDataSource.FindCityListCallback.class));
    }

    @Test
    public void addNewCityToWeatherList() {
        City city = new City("0", "City 0", "Country 0", 100, 200);
        mWeatherRepository.addNewCityToWeatherList(city);

        verify(mLocalDataSource).addNewCityToWeatherList(city);
    }
}
