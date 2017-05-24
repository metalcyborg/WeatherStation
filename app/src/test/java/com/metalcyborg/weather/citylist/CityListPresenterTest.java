package com.metalcyborg.weather.citylist;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.data.source.WeatherRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class CityListPresenterTest {

    private static final List<CityWeather> WEATHER_LIST = new ArrayList<>();
    private static final City CITY_1 = new City("1", "City 1", "Country 1", 10, 20);
    private static final City CITY_2 = new City("2", "City 2", "Country 2", 10, 20);
    private static final Weather WEATHER_1 = new Weather(100);
    private static final Weather WEATHER_2 = new Weather(200);
    private static final CityWeather CITY_WEATHER_1 = new CityWeather(CITY_1, WEATHER_1);
    private static final CityWeather CITY_WEATHER_2 = new CityWeather(CITY_2, WEATHER_2);

    private CityListContract.Presenter mPresenter;

    @Mock
    private WeatherRepository mRepository;

    @Mock
    private CityListContract.View mView;

    @Captor
    private ArgumentCaptor<WeatherDataSource.LoadCityDataCallback> mLoadCityDataCallbackCaptor;

    @Captor
    private ArgumentCaptor<WeatherDataSource.LoadWeatherCallback> mLoadWeatherCallbackCaptor;

    @Captor
    private ArgumentCaptor<CityListContract.ParseCompleteListener> mParseCompleteListenerCaptor;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);

        WEATHER_LIST.add(CITY_WEATHER_1);
        WEATHER_LIST.add(CITY_WEATHER_2);

        mPresenter = new CityListPresenter(mRepository, mView);

        when(mView.isActive()).thenReturn(true);
    }

    @Test
    public void startPresenter_cityListNotAdded() {
        when(mRepository.isCitiesDataAdded()).thenReturn(false);

        mPresenter.start();

        verify(mView).setProgressVisibility(true);
        verify(mView).bindParseService();
    }

    @Test
    public void startPresenter_cityListAdded() {
        when(mRepository.isCitiesDataAdded()).thenReturn(true);

        mPresenter.start();

        verify(mView).setFabVisibility(true);
        verify(mView).setProgressVisibility(true);
        verifyWeatherDataLoading();
    }

    private void verifyWeatherDataLoading() {
        verify(mRepository).loadWeatherData(mLoadWeatherCallbackCaptor.capture());
        mLoadWeatherCallbackCaptor.getValue().onDataListLoaded(WEATHER_LIST);

        verify(mView).showWeatherList(WEATHER_LIST);

        // Data loading error
        mLoadWeatherCallbackCaptor.getValue().onDataListNotAvailable();
        verify(mView, times(2)).setProgressVisibility(false);
        verify(mView).setWeatherLoadingErrorMessageVisibility(true);

        // Weather update
        mLoadWeatherCallbackCaptor.getValue().onDataLoaded(CITY_WEATHER_1.getCity().getOpenWeatherId(),
                WEATHER_1);
        verify(mView).updateItem(CITY_WEATHER_1.getCity().getOpenWeatherId(), WEATHER_1);

        // Weather data not available for the concrete city
        mLoadWeatherCallbackCaptor.getValue().onDataNotAvailable(CITY_1.getOpenWeatherId());
        verify(mView).updateItem(CITY_1.getOpenWeatherId(), null);
    }

    @Test
    public void parseServiceBinded_serviceNotRunning_parseComplete() {
        when(mView.isServiceRunning()).thenReturn(false);

        mPresenter.onParseServiceBound();

        verify(mView).setProgressVisibility(true);
        verify(mView).registerParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verify(mView).parseCitiesData();
        verifyParseComplete();
    }

    @Test
    public void parseServiceBinded_serviceNotRunning_parseError() {
        when(mView.isServiceRunning()).thenReturn(false);

        mPresenter.onParseServiceBound();

        verify(mView).registerParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verify(mView).parseCitiesData();
        verifyParseError();
    }

    @Test
    public void parseServiceBinded_serviceRunning() {
        when(mView.isServiceRunning()).thenReturn(true);

        mPresenter.onParseServiceBound();

        verify(mView).setProgressVisibility(true);
        verify(mView).setParseCitiesDataMessageVisibility(true);
        verify(mView).registerParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verifyParseComplete();
    }

    private void verifyParseComplete() {
        verify(mView).setParseCitiesDataMessageVisibility(true);

        mParseCompleteListenerCaptor.getValue().onParseComplete();
        verify(mView).stopServiceInteractions();
        verify(mRepository).setCitiesDataAdded();
        verify(mView).setParseCitiesDataMessageVisibility(false);
        verify(mView).setFabVisibility(true);
        verifyWeatherDataLoading();
    }

    private void verifyParseError() {
        verify(mView).setProgressVisibility(true);
        verify(mView).setParseCitiesDataMessageVisibility(true);

        mParseCompleteListenerCaptor.getValue().onParseError();
        verify(mView).stopServiceInteractions();
        verify(mView).setParseCitiesDataMessageVisibility(false);
        verify(mView).setParseErrorMessageVisibility(true);
    }

    @Test
    public void fabClick_openCitySearchActivity() {
        mPresenter.addNewCity();

        verify(mView).showCitySearch();
    }

    @Test
    public void stopPresenter() {
        mPresenter.stop();
        verify(mView).stopServiceInteractions();
    }

    @Test
    public void clickSettingsMenuItem() {
        mPresenter.onSettingsMenuItemClick();
        verify(mView).showSettings();
    }
}
