package com.metalcyborg.weather.detail;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.collect.Lists;
import com.metalcyborg.weather.ConnectivityReceiver;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;
import com.metalcyborg.weather.data.source.WeatherDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailPresenterTest {

    private static final City CITY = new City("city_id", "cityName", "countryName",
            10f, 20f, null);
    private static final String TIME_ZONE = "timeZone";

    private static final WeatherDetails WEATHER_DETAILS = new WeatherDetails(100, 100, 100,
            10, 180, 1234, 4321, "icon");
    private static final Weather WEATHER_1 = new Weather(10);
    private static final Weather WEATHER_2 = new Weather(100);
    private static final List<Weather> FORECAST_LIST = Lists.newArrayList(WEATHER_1, WEATHER_2);

    private DetailPresenter mPresenter;

    @Mock
    private WeatherDataSource mRepository;

    @Mock
    private DetailContract.View mView;

    @Mock
    private ConnectivityManager mConnectivityManager;

    @Captor
    ArgumentCaptor<WeatherDataSource.LoadForecastCallback> mLoad3HForecastCallbackCaptor;

    @Captor
    ArgumentCaptor<WeatherDataSource.LoadForecastCallback> mLoad13DForecastCallbackCaptor;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new DetailPresenter(mRepository, mView, mConnectivityManager);
        mPresenter.setParameters(CITY, WEATHER_DETAILS);
        when(mView.isActive()).thenReturn(true);
    }

    @Test
    public void internetConnectionIsMissingOnStart_showMessage() {
        NetworkInfo networkInfo = mock(NetworkInfo.class);
        when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(false);

        mPresenter.start();

        verify(mView).showMissingInternetConnectionMessage();
        verify(mView).registerConnectivityReceiver(any(ConnectivityReceiver.class));
    }

    @Test
    public void loadForecastDataOnStart() {
        mPresenter.start();

        verify(mView).displayCurrentWeatherDetails(CITY.getName(), WEATHER_DETAILS,
                CITY.getTimeZone());
        spy(mPresenter).loadForecastData();
        verify(mView).registerConnectivityReceiver(any(ConnectivityReceiver.class));
    }

    @Test
    public void loadForecastData_dataLoaded() {
        InOrder inOrder = inOrder(mView);
        mPresenter.loadForecastData();

        verify(mRepository).load3HForecastData(anyString(),mLoad3HForecastCallbackCaptor.capture());
        inOrder.verify(mView).setLoadingIndicator(true);
        mLoad3HForecastCallbackCaptor.getValue().onDataLoaded(FORECAST_LIST);

        inOrder.verify(mView).setLoadingIndicator(false);
        verify(mView).show3HForecast(FORECAST_LIST, CITY.getTimeZone());

        verify(mRepository).load13DForecastData(anyString(), mLoad13DForecastCallbackCaptor.capture());
        mLoad13DForecastCallbackCaptor.getValue().onDataLoaded(FORECAST_LIST);

        verify(mView).show13DForecast(FORECAST_LIST, CITY.getTimeZone());
    }

    @Test
    public void loadForecastData_dataNotAvailable() {
        InOrder inOrder = inOrder(mView);
        mPresenter.loadForecastData();

        verify(mRepository).load3HForecastData(anyString(), mLoad3HForecastCallbackCaptor.capture());
        inOrder.verify(mView).setLoadingIndicator(true);
        mLoad3HForecastCallbackCaptor.getValue().onDataNotAvailable();

        inOrder.verify(mView).setLoadingIndicator(false);
        verify(mView).show3hForecastError();

        verify(mRepository).load13DForecastData(anyString(), mLoad13DForecastCallbackCaptor.capture());
        mLoad13DForecastCallbackCaptor.getValue().onDataNotAvailable();

        verify(mView).show13DForecastError();
    }

    @Test
    public void stopPresenter() {
        mPresenter.stop();

        verify(mView).unregisterConnectivityReceiver(any(ConnectivityReceiver.class));
    }

    @Test
    public void connectivityChangedTrue_reloadData() {
        mPresenter.onConnectionChanged(true);

        spy(mPresenter).loadForecastData();
    }

    @Test
    public void connectivityChangedFalse_showMissingConnectionMessage() {
        mPresenter.onConnectionChanged(false);

        verify(mView).showMissingInternetConnectionMessage();
    }
}
