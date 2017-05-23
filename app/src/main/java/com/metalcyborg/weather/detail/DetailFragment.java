package com.metalcyborg.weather.detail;


import android.os.Bundle;
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
import android.widget.TextView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.Utils;
import com.metalcyborg.weather.data.Weather;

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

        mHeaderTemp = (TextView) view.findViewById(R.id.header_temp);
        mHeaderImage = (ImageView) view.findViewById(R.id.header_image);

        mForecastRecycler = (RecyclerView) view.findViewById(R.id.forecastRecycler);
        mForecastRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mForecastAdapter = new ForecastAdapter();
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

    }

    @Override
    public void show13DForecastError() {

    }

    @Override
    public void setLoadingIndicator(boolean indicator) {

    }

    @Override
    public void displayHeader(String cityName, float temperature, String icon) {
        if(mActionBar != null) {
            mActionBar.setTitle(cityName);
            if(temperature != DetailActivity.WRONG_TEMP_VALUE) {
                mHeaderTemp.setText(Utils.getTemperatureString(temperature));
            }

            if(icon != null) {
                int imageId = Utils.getWeatherImageId(icon);
                if(imageId != -1) {
                    mHeaderImage.setImageResource(imageId);
                }
            }
        }
    }
}
