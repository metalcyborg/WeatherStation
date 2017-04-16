package com.metalcyborg.weather.detail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;

public class DetailActivity extends AppCompatActivity {

    private DetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set up the toolbar with the up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        // Add a fragment to the Activity
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (fragment == null) {
            fragment = DetailFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
        }

        mPresenter = new DetailPresenter(
                Injection.provideWeatherRepository(getApplicationContext()),
                fragment
        );
    }
}
