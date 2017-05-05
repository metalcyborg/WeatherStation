package com.metalcyborg.weather.citysearch;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.City;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitySearchFragment extends Fragment implements CitySearchContract.View {

    private static final String TAG = "CitySearch";
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.city_search, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        menu.findItem(R.id.search).expandActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.findCitiesByPartOfTheName(newText);
                Log.d(TAG, "onQueryTextChange: " + newText);
                return true;
            }
        });
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
        for(City city : cityList) {
            Log.d(TAG, "showCityList: " + city.getId() + " " + city.getName());
        }
    }

    @Override
    public void showWeatherList() {

    }
}
