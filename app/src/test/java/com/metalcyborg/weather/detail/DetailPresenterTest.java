package com.metalcyborg.weather.detail;

import com.google.common.collect.Lists;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.WeatherDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailPresenterTest {

    private static final String CITY_ID = "city_id";
    private static final Weather WEATHER_1 = new Weather(10);
    private static final Weather WEATHER_2 = new Weather(100);
    private static final List<Weather> FORECAST_LIST = Lists.newArrayList(WEATHER_1, WEATHER_2);

    private DetailPresenter mPresenter;

    @Mock
    private WeatherDataSource mRepository;

    @Mock
    private DetailContract.View mView;

    @Captor
    ArgumentCaptor<WeatherDataSource.LoadForecastCallback> mLoad3HForecastCallbackCaptor;

    @Captor
    ArgumentCaptor<WeatherDataSource.LoadForecastCallback> mLoad13DForecastCallbackCaptor;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new DetailPresenter(mRepository, mView);
        mPresenter.setParameters(CITY_ID);
        when(mView.isActive()).thenReturn(true);
    }

    @Test
    public void loadForecastData_forecastDataLoaded() {
        mPresenter.start();
        InOrder inOrder = inOrder(mView);

        verify(mRepository).load3HForecastData(eq(CITY_ID),mLoad3HForecastCallbackCaptor.capture());
        inOrder.verify(mView).setLoadingIndicator(true);
        mLoad3HForecastCallbackCaptor.getValue().onDataLoaded(FORECAST_LIST);

        inOrder.verify(mView).setLoadingIndicator(false);
        verify(mView).show3HForecast(FORECAST_LIST);

        verify(mRepository).load13DForecastData(eq(CITY_ID), mLoad13DForecastCallbackCaptor.capture());
        mLoad13DForecastCallbackCaptor.getValue().onDataLoaded(FORECAST_LIST);

        verify(mView).show13DForecast(FORECAST_LIST);
    }

    @Test
    public void loadForecastData_forecastDataNotAvailable() {
        mPresenter.start();
        InOrder inOrder = inOrder(mView);

        verify(mRepository).load3HForecastData(eq(CITY_ID), mLoad3HForecastCallbackCaptor.capture());
        inOrder.verify(mView).setLoadingIndicator(true);
        mLoad3HForecastCallbackCaptor.getValue().onDataNotAvailable();

        inOrder.verify(mView).setLoadingIndicator(false);
        verify(mView).show3hForecastError();

        verify(mRepository).load13DForecastData(eq(CITY_ID), mLoad13DForecastCallbackCaptor.capture());
        mLoad13DForecastCallbackCaptor.getValue().onDataNotAvailable();

        verify(mView).show13DForecastError();
    }
}
