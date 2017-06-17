package com.metalcyborg.weather.citysearch;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.WeatherApp;

import javax.inject.Inject;

public class CitySearchActivity extends AppCompatActivity {

    @Inject
    CitySearchPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        // Add a fragment to the activity
        CitySearchFragment fragment = (CitySearchFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (fragment == null) {
            fragment = CitySearchFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
        }

        DaggerCitySearchComponent.builder()
                .weatherRepositoryComponent(((WeatherApp)getApplication()).getWeatherRepositoryComponent())
                .citySearchPresenterModule(new CitySearchPresenterModule(fragment))
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
