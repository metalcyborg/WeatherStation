package com.metalcyborg.weather.citysearch;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;

public class CitySearchActivity extends AppCompatActivity {

    private CitySearchContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        // Add a fragment to the activity
        CitySearchFragment fragment = (CitySearchFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if(fragment == null) {
            fragment = new CitySearchFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
        }

        mPresenter = new CitySearchPresenter(
                Injection.provideWeatherRepository(getApplicationContext()),
                fragment
        );
    }
}
