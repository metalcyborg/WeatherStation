package com.metalcyborg.weather.citylist;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.metalcyborg.weather.ConnectivityReceiver;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CityListPresenterTest {

    private static final List<CityWeather> WEATHER_LIST = new ArrayList<>();
    private static final City CITY_1 = new City("1", "City 1", "Country 1", 10f, 20f,
            "TimeZone1");
    private static final City CITY_2 = new City("2", "City 2", "Country 2", 10f, 20f,
            "TimeZone2");
    private static final Weather WEATHER_1 = new Weather(100);
    private static final Weather WEATHER_2 = new Weather(200);
    private static final CityWeather CITY_WEATHER_1 = new CityWeather(CITY_1, WEATHER_1);
    private static final CityWeather CITY_WEATHER_2 = new CityWeather(CITY_2, WEATHER_2);

    private CityListPresenter mPresenter;

    @Mock
    private WeatherRepository mRepository;

    @Mock
    private CityListContract.View mView;

    @Mock
    private DbLoader mDbLoader;

    @Mock
    private LoaderManager mLoaderManager;

    @Mock
    private ConnectivityManager mConnectivityManager;

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

        mPresenter = new CityListPresenter(mRepository, mView, mDbLoader, mLoaderManager,
                mConnectivityManager);

        when(mView.isActive()).thenReturn(true);
    }

    @Test
    public void startPresenter_cityListNotAdded() {
        Loader loader = mock(Loader.class);
        when(mRepository.isCitiesDataAdded()).thenReturn(false);
        when(mLoaderManager.initLoader(eq(0), any(Bundle.class), any(LoaderManager.LoaderCallbacks.class)))
                .thenReturn(loader);
        when(loader.isStarted()).thenReturn(false);

        mPresenter.start();

        verify(mView).setProgressVisibility(true);
        verify(loader).forceLoad();

        verify(mView).registerConnectivityReceiver(any(ConnectivityReceiver.class));
    }

    @Test
    public void startPresenter_cityListAdded() {
        when(mRepository.isCitiesDataAdded()).thenReturn(true);

        mPresenter.start();

        verify(mView).setFabVisibility(true);
        verify(mView).setProgressVisibility(true);
        spy(mPresenter).loadCityList();

        verify(mView).registerConnectivityReceiver(any(ConnectivityReceiver.class));
    }

    @Test
    public void internetConnectionIsMissingOnStart_showMessage() {
        when(mRepository.isCitiesDataAdded()).thenReturn(true);

        NetworkInfo networkInfo = mock(NetworkInfo.class);
        when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(false);

        mPresenter.start();

        verify(mView).showMissingInternetConnectionMessage();
        verify(mView).registerConnectivityReceiver(any(ConnectivityReceiver.class));
    }

    @Test
    public void onStop_unregisterConnectivityReceiver() {
        mPresenter.stop();
        verify(mView).unregisterConnectivityReceiver(any(ConnectivityReceiver.class));
    }

    @Test
    public void onDbLoaderLoaded() {
        mPresenter.onLoadFinished(mDbLoader, true);

        verify(mView).setFabVisibility(true);
        spy(mPresenter).loadCityList();
    }

    @Test
    public void onDbLoaderError() {
        mPresenter.onLoadFinished(mDbLoader, false);

        verify(mView).showCopyDatabaseError();
    }

    @Test
    public void loadEmptyCityListFromRepository_showAddCityMessage() {
        mPresenter.loadCityList();

        verify(mRepository).loadWeatherData(mLoadWeatherCallbackCaptor.capture());
        mLoadWeatherCallbackCaptor.getValue().onDataListLoaded(new ArrayList<CityWeather>());

        verify(mView).setProgressVisibility(false);
        verify(mView).showAddCityMessage();
    }

    @Test
    public void loadCityList_displayCurrentWeatherData() {
        mPresenter.loadCityList();

        verify(mRepository).loadWeatherData(mLoadWeatherCallbackCaptor.capture());
        mLoadWeatherCallbackCaptor.getValue().onDataListLoaded(WEATHER_LIST);

        verify(mView).setProgressVisibility(false);
        verify(mView).showWeatherList(WEATHER_LIST);
    }

    @Test
    public void loadCityListWithError_showErrorMessage() {
        mPresenter.loadCityList();

        verify(mRepository).loadWeatherData(mLoadWeatherCallbackCaptor.capture());
        mLoadWeatherCallbackCaptor.getValue().onDataListNotAvailable();

        verify(mView).showWeatherLoadingErrorMessage();
    }

    @Test
    public void updateWeatherData() {
        mPresenter.loadCityList();

        verify(mRepository).loadWeatherData(mLoadWeatherCallbackCaptor.capture());
        mLoadWeatherCallbackCaptor.getValue().onDataLoaded(CITY_1.getOpenWeatherId(),
                WEATHER_1);

        verify(mView).updateItem(CITY_1.getOpenWeatherId(), WEATHER_1);
    }

    @Test
    public void updateWeatherDataWithError() {
        mPresenter.loadCityList();

        verify(mRepository).loadWeatherData(mLoadWeatherCallbackCaptor.capture());
        mLoadWeatherCallbackCaptor.getValue().onDataNotAvailable(CITY_1.getOpenWeatherId());

        verify(mView).updateItem(CITY_1.getOpenWeatherId(), null);
    }

    @Test
    public void fabClick_openCitySearchActivity() {
        mPresenter.addNewCity();

        verify(mView).showCitySearch();
    }

    @Test
    public void stopPresenter() {
        mPresenter.stop();
    }

    @Test
    public void clickSettingsMenuItem() {
        mPresenter.onSettingsMenuItemClick();
        verify(mView).showSettings();
    }

    @Test
    public void deleteSelectedItems() {
        mPresenter.deleteItems(WEATHER_LIST);

        // Delete from list
        verify(mView).deleteSelectedItems();
        // Delete from local db
        verify(mRepository).deleteCitiesFromChosenCityList(anyListOf(City.class));
    }

    @Test
    public void connectivityChangedTrue_reloadData() {
        mPresenter.onConnectionChanged(true);

        spy(mPresenter).loadCityList();
    }

    @Test
    public void connectivityChangedFalse_showMissingConnectionMessage() {
        mPresenter.onConnectionChanged(false);

        verify(mView).showMissingInternetConnectionMessage();
    }
}
