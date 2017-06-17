package com.metalcyborg.weather.citysearch;


import com.metalcyborg.weather.util.FragmentScoped;

import dagger.Module;
import dagger.Provides;

@Module
public final class CitySearchPresenterModule {

    private CitySearchContract.View mView;

    public CitySearchPresenterModule(CitySearchContract.View view) {
        mView = view;
    }

    @Provides
    @FragmentScoped
    public CitySearchContract.View provideCitySearchContractView() {
        return mView;
    }
}
