package com.metalcyborg.weather.citysearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.City;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitySearchFragment extends Fragment implements CitySearchContract.View {

    private CitySearchContract.Presenter mPresenter;

    public CitySearchFragment() {
        // Required empty public constructor
    }

    public static CitySearchFragment newInstance() {
        CitySearchFragment fragment = new CitySearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_search, container, false);
    }

    @Override
    public void setPresenter(CitySearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setProgressVisibility(boolean visibility) {

    }

    @Override
    public void setSearchActionVisibility(boolean visibility) {

    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void showTypeCityNameMessage() {

    }

    @Override
    public void showCityList(List<City> cityList) {

    }

    @Override
    public void showWeatherList() {

    }
}
