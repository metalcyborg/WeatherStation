package com.metalcyborg.weather.citylist;

import com.metalcyborg.weather.data.source.WeatherRepositoryComponent;
import com.metalcyborg.weather.util.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = WeatherRepositoryComponent.class, modules = CityListPresenterModule.class)
public interface CityListComponent {

    void inject(CityListActivity activity);
}
