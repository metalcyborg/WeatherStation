package com.metalcyborg.weather.citylist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;

import com.metalcyborg.weather.data.source.WeatherRepository;
import com.metalcyborg.weather.util.FragmentScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class CityListPresenterModule {

    private CityListContract.View mView;
    private LoaderManager mLoaderManager;
    private ConnectivityManager mConnectivityManager;

    public CityListPresenterModule(CityListContract.View view,
                                     LoaderManager loaderManager,
                                     ConnectivityManager connectivityManager) {
        mView = view;
        mLoaderManager = loaderManager;
        mConnectivityManager = connectivityManager;
    }

    @Provides
    @FragmentScoped
    public CityListContract.View provideCityListContractView() {
        return mView;
    }

    @Provides
    @FragmentScoped
    public LoaderManager provideLoaderManager() {
        return mLoaderManager;
    }

    @Provides
    @FragmentScoped
    public ConnectivityManager provideConnectivityManager() {
        return mConnectivityManager;
    }

    @Provides
    @FragmentScoped
    public AsyncTaskLoader<Boolean> provideDbLoader(Context context,
                                                    WeatherRepository weatherRepository) {
        return new DbLoader(context, weatherRepository);
    }
}
