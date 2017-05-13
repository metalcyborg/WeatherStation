package com.metalcyborg.weather.citylist;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.citylist.parseservice.ParseCitiesService;
import com.metalcyborg.weather.citysearch.CitySearchActivity;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

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
    public void showCitySearch() {
        Intent intent = new Intent(getActivity(), CitySearchActivity.class);
        startActivityForResult(intent, CityListActivity.REQUEST_CITY_SEARCH);
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
        mWeatherAdapter.setItems(weatherList);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode, data == null ? null : data.getExtras());
    }
}
