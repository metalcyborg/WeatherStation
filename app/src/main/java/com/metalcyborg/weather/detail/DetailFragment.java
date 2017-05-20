package com.metalcyborg.weather.detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.Weather;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements DetailContract.View {

    private DetailContract.Presenter mPresenter;
    private RecyclerView mForecastRecycler;
    private ForecastAdapter mForecastAdapter;


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
}
