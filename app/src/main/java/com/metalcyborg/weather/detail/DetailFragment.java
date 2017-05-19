package com.metalcyborg.weather.detail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.Weather;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements DetailContract.View {

    private DetailContract.Presenter mPresenter;
    private RecyclerView mForecast13DRecycler;
    private DayForecastAdapter mDayForecastAdapter;


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
        mForecast13DRecycler = (RecyclerView) view.findViewById(R.id.forecast13DRecycler);
        mForecast13DRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDayForecastAdapter = new DayForecastAdapter();
        mForecast13DRecycler.setAdapter(mDayForecastAdapter);

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

    }

    @Override
    public void show13DForecast(List<Weather> forecast) {
        mDayForecastAdapter.setItems(forecast);
        mDayForecastAdapter.notifyDataSetChanged();
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
