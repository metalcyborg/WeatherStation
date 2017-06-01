package com.metalcyborg.weather.citylist;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.util.EspressoIdlingResource;
import com.metalcyborg.weather.util.WeatherUtils;
import com.metalcyborg.weather.citylist.parseservice.ParseCitiesService;
import com.metalcyborg.weather.citysearch.CitySearchActivity;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;
import com.metalcyborg.weather.detail.DetailActivity;
import com.metalcyborg.weather.settings.SettingsActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityListFragment extends Fragment implements CityListContract.View {

    private static final String CITY_LIST_ZIP_NAME = "cityList";
    private CityListContract.Presenter mPresenter;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private WeatherAdapter mWeatherAdapter;
    private ServiceConnection mServiceConnection;
    private ParseCitiesService.ParseBinder mServiceBinder;
    private Handler mUiHandler = new Handler();
    private ActionMode mActionMode = null;
    private ActionMode.Callback mActionModeCallback = null;

    public CityListFragment() {
        // Required empty public constructor
    }

    public static CityListFragment newInstance() {
        CityListFragment fragment =  new CityListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);

        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewCity();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mWeatherAdapter = new WeatherAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mWeatherAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mActionMode = mode;
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.city_list_action_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.getItemId() == R.id.delete) {
                    mPresenter.deleteItems(mWeatherAdapter.getSelectedItems());
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
                mWeatherAdapter.clearItemSelection();
                mWeatherAdapter.notifyDataSetChanged();
            }
        };

        mWeatherAdapter.setOnItemClickListener(new WeatherAdapter.WeatherClickListener() {
            @Override
            public void onClick(CityWeather cityWeather, int position) {
                if(mActionMode != null) {
                    // Select item
                    int count = mWeatherAdapter.changeItemSelection(position);
                    mWeatherAdapter.notifyItemChanged(position);
                    if(count == 0) {
                        mActionMode.finish();
                    } else {
                        mActionMode.setTitle(String.valueOf(count));
                    }
                } else {
                    mPresenter.onWeatherItemClicked(cityWeather);
                }
            }

            @Override
            public void onLongClick(CityWeather cityWeather,int position) {
                if(mActionMode != null) {
                    return;
                }

                mPresenter.onWeatherItemLongClicked(cityWeather);
                ((AppCompatActivity)getActivity()).startSupportActionMode(mActionModeCallback);

                int count = mWeatherAdapter.changeItemSelection(position);
                mWeatherAdapter.notifyItemChanged(position);
                mActionMode.setTitle(String.valueOf(count));
            }
        });

        return view;
    }

    @Override
    public void setPresenter(CityListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.city_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings) {
            // Open settings
            mPresenter.onSettingsMenuItemClick();
        }

        return true;
    }

    @Override
    public void showCitySearch() {
        Intent intent = new Intent(getActivity(), CitySearchActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setProgressVisibility(boolean visibility) {

    }

    @Override
    public void setParseCitiesDataMessageVisibility(boolean visibility) {

    }

    @Override
    public void setParseErrorMessageVisibility(boolean visibility) {

    }

    @Override
    public void setWeatherLoadingErrorMessageVisibility(boolean visibility) {

    }

    @Override
    public void setFabVisibility(boolean visibility) {
        mFab.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showWeatherList(List<CityWeather> weatherList) {
        mWeatherAdapter.setItems(weatherList, WeatherUtils.getCurrentTempUnits(getActivity()));
        mWeatherAdapter.notifyDataSetChanged();
    }

    @Override
    public void bindParseService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mServiceBinder = (ParseCitiesService.ParseBinder) service;
                mPresenter.onParseServiceBound();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        ParseCitiesService.bind(getActivity().getApplicationContext(), mServiceConnection);
    }

    @Override
    public boolean isServiceRunning() {
        if(mServiceBinder != null) {
            return mServiceBinder.getService().isRunning();
        }

        return false;
    }

    @Override
    public void parseCitiesData() {
        ParseCitiesService.startActionParse(getActivity().getApplicationContext(),
                CITY_LIST_ZIP_NAME);
    }

    @Override
    public void registerParseCompleteListener(final CityListContract.ParseCompleteListener listener) {
        if(mServiceBinder == null)
            return;

        mServiceBinder.registerParseCompleteListener(new ParseCitiesService.CompleteListener() {
            @Override
            public void onParseComplete() {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onParseComplete();
                    }
                });
            }

            @Override
            public void onParseError() {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onParseError();
                    }
                });
            }
        });
    }

    @Override
    public void stopServiceInteractions() {
        if(mServiceBinder == null || mServiceConnection == null)
            return;

        mServiceBinder.unregisterParseCompleteListener();
        ParseCitiesService.unbind(getActivity().getApplicationContext(), mServiceConnection);
        mServiceBinder = null;
        mServiceConnection = null;
    }

    @Override
    public void updateItem(String cityId, Weather weather) {
        mWeatherAdapter.updateItem(cityId, weather);
    }

    @Override
    public void showForecast(CityWeather cityWeather) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        City city = cityWeather.getCity();
        Weather weather = cityWeather.getWeather();
        WeatherDetails details = new WeatherDetails();
        if(weather != null) {
            if(weather.getMain() != null ) {
                details.setTemperature(weather.getMain().getDayTemp());
                details.setPressure(weather.getMain().getPressure());
                details.setHumidity(weather.getMain().getHumidity());
            }
            if(weather.getWeatherDescription() != null) {
                details.setIcon(weather.getWeatherDescription().getIcon());
            }

            if(weather.getSys() != null) {
                details.setSunrise(weather.getSys().getSunrise());
                details.setSunset(weather.getSys().getSunset());
            }

            if(weather.getWind() != null) {
                details.setWindSpeed(weather.getWind().getSpeed());
                details.setWindDeg(weather.getWind().getDeg());
            }
        }

        intent.putExtra(DetailActivity.EXTRAS_WEATHER_DETAILS, details);

        if(city != null) {
            intent.putExtra(DetailActivity.EXTRAS_CITY_ID, cityWeather.getCity().getOpenWeatherId());
            intent.putExtra(DetailActivity.EXTRAS_CITY_NAME, cityWeather.getCity().getName());
            startActivity(intent);
        }
    }

    @Override
    public void showSettings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteSelectedItems() {
        SparseBooleanArray selectedItems = mWeatherAdapter.getSelectedPositions();
        mWeatherAdapter.deleteSelectedItems();

        for(int i = 0; i < selectedItems.size(); ++i) {
            // FIXME: animation not running
            int position = selectedItems.keyAt(i);
            mWeatherAdapter.notifyItemRemoved(position);
        }

        mWeatherAdapter.clearItemSelection();

        if(mActionMode != null) {
            mActionMode.finish();
        }
    }
}
