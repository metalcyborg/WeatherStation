package com.metalcyborg.weather.detail;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.WeatherDetails;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRAS_CITY = "city";
    public static final String EXTRAS_WEATHER_DETAILS = "weatherDetails";
    public static final float WRONG_TEMP_VALUE = -1000f;
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
                fragment,
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)
        );

        // Extras
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            City city = extras.getParcelable(DetailActivity.EXTRAS_CITY);
            WeatherDetails details = extras.getParcelable(EXTRAS_WEATHER_DETAILS);

            mPresenter.setParameters(city, details);
        }
    }
}
