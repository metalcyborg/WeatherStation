package com.metalcyborg.weather.citylist;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.citysearch.CitySearchActivity;
import com.metalcyborg.weather.data.Weather;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityListFragment extends Fragment implements CityListContract.View {

    private CityListContract.Presenter mPresenter;
    private FloatingActionButton mFab;

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
    public void setFabVisibility(boolean visibility) {

    }

    @Override
    public void showWeatherList(List<Weather> weatherList) {

    }

    @Override
    public void bindParseService() {

    }

    @Override
    public void unbindParseService() {

    }

    @Override
    public boolean isServiceRunning() {
        return false;
    }

    @Override
    public void parseCitiesData() {

    }

    @Override
    public void registerParseCompleteListener(CityListContract.ParseCompleteListener listener) {

    }

    @Override
    public void unregisterParseCompleteListener(CityListContract.ParseCompleteListener listener) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode, data == null ? null : data.getExtras());
    }
}
