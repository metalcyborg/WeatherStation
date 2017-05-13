package com.metalcyborg.weather.citysearch;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.data.source.WeatherRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by metalcyborg on 17.04.17.
 */

public class CitySearchPresenterTest {

    private static List<City> CITIES = new ArrayList<>();

    private CitySearchContract.Presenter mPresenter;

    @Mock
    private WeatherRepository mRepository;

    @Mock
    private CitySearchContract.View mView;

    @Captor
    private ArgumentCaptor<WeatherDataSource.LoadCityDataCallback> mCityDataCallbackCaptor;

    @Captor
    private ArgumentCaptor<WeatherDataSource.FindCityListCallback> mFindCityListCallbackCaptor;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new CitySearchPresenter(mRepository, mView);

        when(mView.isActive()).thenReturn(true);

        CITIES.add(new City("0", "City 0", "Country 0", 100, 200));
        CITIES.add(new City("1", "City 1", "Country 1", 300, 400));
        CITIES.add(new City("2", "City 2", "Country 2", 500, 600));
    }

    @Test
    public void addCitiesDataToModel() {
        when(mRepository.isCitiesDataAdded()).thenReturn(false);

        mPresenter.start();
    }

    @Test
    public void addCitiesDataToModel_error() {
        when(mRepository.isCitiesDataAdded()).thenReturn(false);

        mPresenter.start();
    }

    @Test
    public void findCitiesByName() {
        String partOfTheName = "Abc";
        mPresenter.findCitiesByPartOfTheName(partOfTheName);

        verify(mRepository).findCitiesByPartOfTheName(eq(partOfTheName), anyInt(),
                mFindCityListCallbackCaptor.capture());
        mFindCityListCallbackCaptor.getValue().onDataFound(CITIES);
        verify(mView).showCityList(CITIES);
    }

    @Test
    public void findCityByName_emptyName() {
        mPresenter.findCitiesByPartOfTheName("");

        verify(mView, never()).showCityList(Mockito.anyListOf(City.class));
    }

    @Test
    public void clickOnCity_showCityListActivity() {
        City city = new City("0", "City 0", "Country 0", 100, 200);
        mPresenter.addCityToWeatherList(city);
        verify(mRepository).addNewCityToChosenCityList(city);
        verify(mView).showWeatherList();
    }
}
