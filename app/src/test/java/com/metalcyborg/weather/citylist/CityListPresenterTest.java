package com.metalcyborg.weather.citylist;

import com.metalcyborg.weather.citylist.CityListContract;
import com.metalcyborg.weather.citylist.CityListPresenter;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class CityListPresenterTest {

    private static final List<Weather> WEATHER_LIST = new ArrayList<>();
    private static final Weather WEATHER_1 = new Weather();
    private static final Weather WEATHER_2 = new Weather();

    private CityListContract.Presenter mPresenter;

    @Mock
    private WeatherRepository mRepository;

    @Mock
    private CityListContract.View mView;

    @Captor
    private ArgumentCaptor<WeatherDataSource.LoadCityDataCallback> mLoadCityDataCallbackCaptor;

    @Captor
    private ArgumentCaptor<WeatherDataSource.LoadWeatherListCallback> mLoadWeatherListCallbackCaptor;

    @Captor
    private ArgumentCaptor<CityListContract.ParseCompleteListener> mParseCompleteListenerCaptor;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);

        WEATHER_LIST.add(WEATHER_1);
        WEATHER_LIST.add(WEATHER_2);

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

        verifyWeatherDataLoading();
    }

    private void verifyWeatherDataLoading() {
        verify(mView).setProgressVisibility(true);
        verify(mRepository).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
        mLoadWeatherListCallbackCaptor.getValue().onDataLoaded(WEATHER_LIST);

        verify(mView).showWeatherList(WEATHER_LIST);
        verify(mView).setProgressVisibility(false);
        verify(mView).setFabVisibility(true);
    }

    @Test
    public void parseServiceBinded_serviceNotRunning_parseComplete() {
        when(mView.isServiceRunning()).thenReturn(false);

        mPresenter.onParseServiceBinded();

        verify(mView).registerParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verify(mView).parseCitiesData();
        verifyParseComplete();
    }

    @Test
    public void parseServiceBinded_serviceNotRunning_parseError() {
        when(mView.isServiceRunning()).thenReturn(false);

        mPresenter.onParseServiceBinded();

        verify(mView).registerParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verify(mView).parseCitiesData();
        verifyParseError();
    }

    @Test
    public void parseServiceBinded_serviceRunning() {
        when(mView.isServiceRunning()).thenReturn(true);

        mPresenter.onParseServiceBinded();

        verify(mView).registerParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verifyParseComplete();
    }

    private void verifyParseComplete() {
        verify(mView).setProgressVisibility(true);
        verify(mView).setParseCitiesDataMessageVisibility(true);

        mParseCompleteListenerCaptor.getValue().onParseComplete();
        verify(mView).setParseCitiesDataMessageVisibility(false);
        verifyWeatherDataLoading();
    }

    private void verifyParseError() {
        verify(mView).setProgressVisibility(true);
        verify(mView).setParseCitiesDataMessageVisibility(true);

        mParseCompleteListenerCaptor.getValue().onParseError();
        verify(mView).setParseCitiesDataMessageVisibility(false);
        verify(mView).setParseErrorMessageVisibility(true);
    }

    @Test
    public void fabClick_openCitySearchActivity() {
        mPresenter.addNewCity();

        verify(mView).showCitySearch();
    }

    @Test
    public void stopService() {
        mPresenter.stop();
        verify(mView).unregisterParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verify(mView).unbindParseService();
    }
}
