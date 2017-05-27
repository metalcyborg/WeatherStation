package com.metalcyborg.weather.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.WeatherDetails;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRAS_CITY_ID = "cityId";
    public static final String EXTRAS_CITY_NAME = "cityName";
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
                fragment
        );

        // Extras
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String cityId = extras.getString(EXTRAS_CITY_ID, "");
            String cityName = extras.getString(EXTRAS_CITY_NAME, "");
            WeatherDetails details = extras.getParcelable(EXTRAS_WEATHER_DETAILS);

            mPresenter.setParameters(cityId, cityName, details);
        }
    }
}
