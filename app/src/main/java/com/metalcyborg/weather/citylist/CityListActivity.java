package com.metalcyborg.weather.citylist;

import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.source.WeatherDataSource;

public class CityListActivity extends AppCompatActivity {

    private CityListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        // Set default settings values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

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

        WeatherDataSource weatherRepository = Injection.provideWeatherRepository(getApplicationContext());
        mPresenter = new CityListPresenter(
                weatherRepository,
                fragment,
                new DbLoader(getApplicationContext(), weatherRepository),
                getSupportLoaderManager(),
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE));
    }
}
