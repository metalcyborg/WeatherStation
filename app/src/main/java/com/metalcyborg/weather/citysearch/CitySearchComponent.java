package com.metalcyborg.weather.citysearch;

import com.metalcyborg.weather.citylist.CityListPresenterModule;
import com.metalcyborg.weather.data.source.WeatherRepositoryComponent;
import com.metalcyborg.weather.util.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = WeatherRepositoryComponent.class, modules = CitySearchPresenterModule.class)
public interface CitySearchComponent {

    void inject(CitySearchActivity activity);
}
