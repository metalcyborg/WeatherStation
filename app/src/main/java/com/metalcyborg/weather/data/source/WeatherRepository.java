package com.metalcyborg.weather.data.source;

import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.metalcyborg.weather.citylist.parseservice.CityData;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.local.LocalDataSource;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.remote.RemoteDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class WeatherRepository implements WeatherDataSource {

    private static volatile WeatherRepository mInstance;
    private LocalDataSource mLocalDataSource;
    private RemoteDataSource mRemoteDataSource;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Map<String, CityWeather> mCachedWeather;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Map<String, List<Weather>> mCached3hForecast;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Map<String, List<Weather>> mCached13DForecast;

    private WeatherRepository(@NonNull LocalDataSource localDataSource,
                              @NonNull RemoteDataSource remoteDataSource) {
        mLocalDataSource = checkNotNull(localDataSource);
        mRemoteDataSource = checkNotNull(remoteDataSource);
    }

    public static WeatherRepository getInstance(LocalDataSource localDataSource,
                                                RemoteDataSource remoteDataSource) {
        if (mInstance == null) {
            synchronized (WeatherRepository.class) {
                if (mInstance == null) {
                    mInstance = new WeatherRepository(localDataSource, remoteDataSource);
                }
            }
        }

        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }

    @Override
    public boolean isCitiesDataAdded() {
        return mLocalDataSource.isCitiesDataAdded();
    }

    @Override
    public void setCitiesDataAdded() {
        mLocalDataSource.setCitiesDataAdded();
    }

    @Override
    public void addCitiesData(CityData[] data) {
        mLocalDataSource.addCitiesData(data);
    }

    @Override
    public void loadWeatherData(final LoadWeatherCallback callback) {
        if (mCachedWeather != null) {
            List<CityWeather> weatherList = new ArrayList<>(mCachedWeather.values());
            // Load from cache
            callback.onDataListLoaded(weatherList);
            // Check remote source
            checkCurrentWeatherOnServer(weatherList, callback);
        } else {
            // Load from local DB
            mLocalDataSource.loadWeatherData(new LocalDataSource.LoadWeatherListCallback() {
                @Override
                public void onDataLoaded(List<CityWeather> weatherData) {
                    // Local data found
                    callback.onDataListLoaded(weatherData);
                    refreshCurrentWeatherCache(weatherData);
                    // Check remote source
                    checkCurrentWeatherOnServer(weatherData, callback);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataListNotAvailable();
                }
            });
        }
    }

    @Override
    public void load3HForecastData(final String cityId, final LoadForecastCallback callback) {
        if(mCached3hForecast != null && mCached3hForecast.get(cityId) != null) {
            List<Weather> forecast = mCached3hForecast.get(cityId);
            // Load from cache
            callback.onDataLoaded(forecast);
        } else {
            // Load from local db
            mLocalDataSource.load3HForecastData(cityId, new LocalDataSource.LoadForecastCallback() {
                @Override
                public void onDataLoaded(List<Weather> forecast) {
                    // Local data found
                    callback.onDataLoaded(forecast);
                    addTo3HForecastHash(cityId, forecast);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }

        // Check remote source
        check3HForecastOnServer(cityId, callback);
    }

    @Override
    public void load13DForecastData(final String cityId, final LoadForecastCallback callback) {
        if(mCached13DForecast != null && mCached13DForecast.get(cityId) != null) {
            List<Weather> forecast = mCached13DForecast.get(cityId);
            // Load from cache
            callback.onDataLoaded(forecast);
        } else {
            // Load from local db
            mLocalDataSource.load13DForecastData(cityId, new LocalDataSource.LoadForecastCallback() {
                @Override
                public void onDataLoaded(List<Weather> forecast) {
                    // Local data found
                    callback.onDataLoaded(forecast);
                    addTo13DForecastHash(cityId, forecast);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }

        // Check remote source
        check13DForecastOnServer(cityId, callback);
    }

    private void refreshCurrentWeatherCache(List<CityWeather> weatherList) {
        if(mCachedWeather == null) {
            mCachedWeather = new LinkedHashMap<>();
        }
        mCachedWeather.clear();
        for(CityWeather cityWeather : weatherList) {
            mCachedWeather.put(cityWeather.getCity().getOpenWeatherId(), cityWeather);
        }
    }

    private void addToCurrentWeatherCache(CityWeather cityWeather) {
        if(mCachedWeather == null) {
            mCachedWeather = new LinkedHashMap<>();
        }

        mCachedWeather.put(cityWeather.getCity().getOpenWeatherId(), cityWeather);
    }

    private void updateCachedCurentWeather(String cityId, Weather weather) {
        if(mCachedWeather != null && mCachedWeather.get(cityId) != null) {
            mCachedWeather.get(cityId).setWeather(weather);
        }
    }

    private void addTo3HForecastHash(String cityId, List<Weather> forecast) {
        if(mCached3hForecast == null) {
            mCached3hForecast = new LinkedHashMap<>();
        }

        mCached3hForecast.put(cityId, forecast);
    }

    private void addTo13DForecastHash(String cityId, List<Weather> forecast) {
        if(mCached13DForecast == null) {
            mCached13DForecast = new LinkedHashMap<>();
        }

        mCached13DForecast.put(cityId, forecast);
    }

    private void checkCurrentWeatherOnServer(List<CityWeather> weatherList, final LoadWeatherCallback callback) {
        for (final CityWeather cityWeather : weatherList) {
            mRemoteDataSource.loadWeatherData(cityWeather.getCity().getOpenWeatherId(),
                    new RemoteDataSource.GetWeatherCallback() {
                        @Override
                        public void onDataLoaded(String cityId, Weather weather) {
                            mLocalDataSource.updateCurrentWeather(cityId, weather);
                            updateCachedCurentWeather(cityId, weather);
                            callback.onDataLoaded(cityId, weather);
                        }

                        @Override
                        public void onDataNotAvailable(String cityId) {
                            if(cityWeather.getWeather() == null) {
                                callback.onDataNotAvailable(cityId);
                            }
                        }
                    });
        }
    }

    private void check3HForecastOnServer(final String cityId, final LoadForecastCallback callback) {
        mRemoteDataSource.load3HForecastData(cityId, new RemoteDataSource.GetForecastCallback() {
            @Override
            public void onDataLoaded(List<Weather> forecast) {
                mLocalDataSource.update3HForecast(cityId, forecast);
                addTo3HForecastHash(cityId, forecast);
                callback.onDataLoaded(forecast);
            }

            @Override
            public void onDataNotAvailable() {
                if(mCached3hForecast == null ||
                        mCached3hForecast.get(cityId) == null) {
                    callback.onDataNotAvailable();
                }
            }
        });
    }

    private void check13DForecastOnServer(final String cityId, final LoadForecastCallback callback) {
        mRemoteDataSource.load13DForecastData(cityId, new RemoteDataSource.GetForecastCallback() {
            @Override
            public void onDataLoaded(List<Weather> forecast) {
                mLocalDataSource.update13DForecast(cityId, forecast);
                addTo13DForecastHash(cityId, forecast);
                callback.onDataLoaded(forecast);
            }

            @Override
            public void onDataNotAvailable() {
                if(mCached13DForecast == null ||
                        mCached13DForecast.get(cityId) == null) {
                    callback.onDataNotAvailable();
                }
            }
        });
    }

    @Override
    public void findCitiesByPartOfTheName(String partOfTheName, int count,
                                          final FindCityListCallback callback) {
        mLocalDataSource.findCitiesByPartOfTheName(partOfTheName, count,
                new LocalDataSource.FindCityListCallback() {
                    @Override
                    public void onDataFound(List<City> cityList) {
                        callback.onDataFound(cityList);
                    }
                });
    }

    @Override
    public void addNewCityToChosenCityList(City city) throws SQLiteException {
        mLocalDataSource.addNewCityToChosenCityList(city);
        CityWeather cityWeather = new CityWeather(city, null);
        addToCurrentWeatherCache(cityWeather);
    }

    @Override
    public void deleteCitiesFromChosenCityList(List<CityWeather> cityWeatherList) {

    }
}
