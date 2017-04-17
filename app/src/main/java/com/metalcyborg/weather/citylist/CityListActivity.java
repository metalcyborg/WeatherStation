package com.metalcyborg.weather.citylist;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;

public class CityListActivity extends AppCompatActivity {

    public static final int REQUEST_CITY_SEARCH = 1;
    private CityListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setHomeButtonEnabled(true);

        CityListFragment fragment = (CityListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if(fragment == null) {
            fragment = CityListFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
        }

        mPresenter = new CityListPresenter(
                Injection.provideWeatherRepository(getApplicationContext()),
                fragment);
    }
}
