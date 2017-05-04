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
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

        verify(mView).setProgressVisibility(true);
        verifyWeatherDataLoading();
    }

    private void verifyWeatherDataLoading() {
        verify(mRepository).loadWeatherData(mLoadWeatherListCallbackCaptor.capture());
        mLoadWeatherListCallbackCaptor.getValue().onDataLoaded(WEATHER_LIST);

        verify(mView).showWeatherList(WEATHER_LIST);
        verify(mView).setFabVisibility(true);

        // Data loading error
        mLoadWeatherListCallbackCaptor.getValue().onError();
        verify(mView, times(2)).setProgressVisibility(false);
        verify(mView).setWeatherLoadingErrorMessageVisibility(true);
    }

    @Test
    public void parseServiceBinded_serviceNotRunning_parseComplete() {
        when(mView.isServiceRunning()).thenReturn(false);

        mPresenter.onParseServiceBinded();

        verify(mView).setProgressVisibility(true);
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

        verify(mView).setProgressVisibility(true);
        verify(mView).setParseCitiesDataMessageVisibility(true);
        verify(mView).registerParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verifyParseComplete();
    }

    private void verifyParseComplete() {
        verify(mView).setParseCitiesDataMessageVisibility(true);

        mParseCompleteListenerCaptor.getValue().onParseComplete();
        verify(mView).unregisterParseCompleteListener(mParseCompleteListenerCaptor.getValue());
        verify(mView).unbindParseService();
        verify(mRepository).setCitiesDataAdded();
        verify(mView).setParseCitiesDataMessageVisibility(false);
        verifyWeatherDataLoading();
    }

    private void verifyParseError() {
        verify(mView).setProgressVisibility(true);
        verify(mView).setParseCitiesDataMessageVisibility(true);

        mParseCompleteListenerCaptor.getValue().onParseError();
        verify(mView).unregisterParseCompleteListener(mParseCompleteListenerCaptor.getValue());
        verify(mView).unbindParseService();
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
        when(mView.isBindedWithParseService()).thenReturn(true);

        mPresenter.stop();
        verify(mView).unregisterParseCompleteListener(mParseCompleteListenerCaptor.capture());
        verify(mView).unbindParseService();
    }
}
