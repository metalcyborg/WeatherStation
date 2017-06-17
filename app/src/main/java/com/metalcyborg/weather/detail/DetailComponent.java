package com.metalcyborg.weather.detail;


import com.metalcyborg.weather.data.source.WeatherRepositoryComponent;
import com.metalcyborg.weather.util.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = WeatherRepositoryComponent.class, modules = DetailPresenterModule.class)
public interface DetailComponent {

    void inject(DetailActivity activity);
}
