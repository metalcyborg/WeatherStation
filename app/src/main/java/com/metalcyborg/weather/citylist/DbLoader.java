package com.metalcyborg.weather.citylist;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.data.source.local.WeatherDatabaseHelper;
import com.metalcyborg.weather.util.FragmentScoped;
import com.metalcyborg.weather.util.WeatherUtils;

import java.io.IOException;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

@FragmentScoped
public class DbLoader extends AsyncTaskLoader<Boolean> {

    private WeatherDataSource mWeatherRepository;

    @Inject
    public DbLoader(Context context, WeatherDataSource repository) {
        super(context);
        mWeatherRepository = checkNotNull(repository, "Repository cannot be null");
    }

    @Override
    public Boolean loadInBackground() {
        try {
            WeatherUtils.copyDatabaseFromAssets(getContext(), WeatherDatabaseHelper.DB_NAME);
            mWeatherRepository.setCitiesDataAdded();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


}
