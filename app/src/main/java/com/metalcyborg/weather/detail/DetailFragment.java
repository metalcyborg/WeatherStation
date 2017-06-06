package com.metalcyborg.weather.detail;


import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.metalcyborg.weather.ConnectivityReceiver;
import com.metalcyborg.weather.R;
import com.metalcyborg.weather.util.WeatherUtils;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements DetailContract.View {

    private DetailContract.Presenter mPresenter;
    private RecyclerView mForecastRecycler;
    private ForecastAdapter mForecastAdapter;
    private ActionBar mActionBar;
    private TextView mHeaderTemp;
    private ImageView mHeaderImage;
    private ImageView mHeaderIcon;
    private ProgressBar mProgressBar;
    private CoordinatorLayout mCoordinatorLayout;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Set up the toolbar with the up button
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setTitle("");
        }

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);

        mHeaderTemp = (TextView) view.findViewById(R.id.header_temp);
        mHeaderImage = (ImageView) view.findViewById(R.id.header_image);
        mHeaderIcon = (ImageView) view.findViewById(R.id.header_icon);

        mForecastRecycler = (RecyclerView) view.findViewById(R.id.forecastRecycler);
        mForecastRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mForecastAdapter = new ForecastAdapter(WeatherUtils.getCurrentTempUnits(getActivity()),
                WeatherUtils.getCurrentPressureUnits(getActivity()),
                WeatherUtils.getCurrentSpeedUnits(getActivity()),
                WeatherUtils.getCurrentTimeUnits(getActivity()));
        mForecastRecycler.setAdapter(mForecastAdapter);

        return view;
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
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void show3HForecast(List<Weather> forecast) {
        mForecastAdapter.set3HForecast(forecast);
        mForecastAdapter.notifyItemChanged(0);
    }

    @Override
    public void show13DForecast(List<Weather> forecast) {
        mForecastAdapter.setDayForecast(forecast);
        mForecastAdapter.notifyDataSetChanged();
    }

    @Override
    public void show3hForecastError() {
        mForecastAdapter.show3HForecastError();
        mForecastAdapter.notifyDataSetChanged();
    }

    @Override
    public void show13DForecastError() {
        mForecastAdapter.showDayForecastError();
        mForecastAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMissingInternetConnectionMessage() {
        showSnackbarError(R.string.missing_internet);
    }

    @Override
    public void setLoadingIndicator(boolean indicator) {
        mProgressBar.setVisibility(indicator ? View.VISIBLE : View.GONE);
    }

    @Override
    public void displayCurrentWeatherDetails(String cityName, WeatherDetails details) {
        if(mActionBar != null) {
            mActionBar.setTitle(cityName);
            if(details.getTemperature() != DetailActivity.WRONG_TEMP_VALUE) {
                mHeaderTemp.setText(WeatherUtils.getTemperatureString(details.getTemperature(),
                        WeatherUtils.getCurrentTempUnits(getActivity())));
            }

            if(details.getIcon() != null) {
                int imageId = WeatherUtils.getWeatherImageId(details.getIcon());
                if(imageId != -1) {
                    mHeaderIcon.setImageResource(imageId);
                }
                if(WeatherUtils.isNightIcon(details.getIcon())) {
                    mHeaderImage.setBackgroundColor(
                            getResources().getColor(R.color.toolbar_night_background));
                } else {
                    mHeaderImage.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            } else {
                mHeaderImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            mForecastAdapter.setWeatherDetails(details);
        }
    }

    @Override
    public void registerConnectivityReceiver(ConnectivityReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterConnectivityReceiver(ConnectivityReceiver receiver) {
        getActivity().unregisterReceiver(receiver);
    }

    private void showSnackbarError(int textResource) {
        Snackbar.make(mCoordinatorLayout, textResource, Snackbar.LENGTH_LONG).show();
    }
}
