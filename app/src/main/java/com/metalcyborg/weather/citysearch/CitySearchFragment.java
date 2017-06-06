package com.metalcyborg.weather.citysearch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private RecyclerView mRecyclerView;
    private CityAdapter mCityAdapter;

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
        View view = inflater.inflate(R.layout.fragment_city_search, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.cityRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCityAdapter = new CityAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mCityAdapter);

        mCityAdapter.setCityClickListener(new CityAdapter.CityClickListener() {
            @Override
            public void onClick(City city) {
                mPresenter.addCityToWeatherList(city);
            }
        });

        return view;
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

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.findCitiesByPartOfTheName(newText);
                return true;
            }
        });

        MenuItemCompat.expandActionView(searchItem);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getActivity().finish();
                return false;
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
        mCityAdapter.setCityList(cityList);
        mCityAdapter.notifyDataSetChanged();
    }

    @Override
    public void showWeatherList() {
        getActivity().finish();
    }


}
