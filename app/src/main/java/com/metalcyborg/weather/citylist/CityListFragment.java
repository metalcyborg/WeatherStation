package com.metalcyborg.weather.citylist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metalcyborg.weather.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityListFragment extends Fragment implements CityListContract.View {

    private CityListContract.Presenter mPresenter;

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
        return inflater.inflate(R.layout.fragment_blank, container, false);
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
}
