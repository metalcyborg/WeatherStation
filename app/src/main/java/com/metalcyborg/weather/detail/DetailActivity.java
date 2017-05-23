package com.metalcyborg.weather.detail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRAS_CITY_ID = "cityId";
    private DetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        // Extras
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String cityId = extras.getString(EXTRAS_CITY_ID, "");

            mPresenter.setParameters(cityId);
        }
    }
}
