package com.metalcyborg.weather.citylist.parseservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteException;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.data.source.WeatherDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class ParseCitiesService extends IntentService {
    private static final String ACTION_PARSE = "com.metalcyborg.weather.citysearch.action.PARSE";

    private static final String EXTRA_ZIP_NAME = "com.metalcyborg.weather.citysearch.extra.ZIP_NAME";
    private static final String TAG = "ParseCitiesService";

    private volatile boolean mRunning = false;
    private IBinder mBinder = new ParseBinder();
    private CompleteListener mCompleteListener;
    private WeatherDataSource mRepository;

    public interface CompleteListener {
        void onParseComplete();

        void onParseError();
    }

    public ParseCitiesService() {
        super("ParseCitiesService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRepository = Injection.provideWeatherRepository(getApplicationContext());
    }

    public static void startActionParse(Context context, String zipName) {
        Intent intent = new Intent(context, ParseCitiesService.class);
        intent.setAction(ACTION_PARSE);
        intent.putExtra(EXTRA_ZIP_NAME, zipName);
        context.startService(intent);
    }

    public static void bind(Context context, ServiceConnection serviceConnection) {
        Intent intent = new Intent(context, ParseCitiesService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public static void unbind(Context context, ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PARSE.equals(action)) {
                mRunning = true;
                Log.d(TAG, "onHandleIntent: Parse");
                final String zipName = intent.getStringExtra(EXTRA_ZIP_NAME);
                handleActionUnzip(zipName);
            }
        }
    }

    public void handleActionUnzip(String zipName) {
        InputStream is = null;
        GZIPInputStream gis = null;
        try {
            is = getAssets().open(zipName);
            gis = new GZIPInputStream(is);
            Gson gson = new GsonBuilder()
                    .create();
            JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(gis));
            jsonReader.setLenient(true);

            CityData[] cityData = gson.fromJson(jsonReader, CityData[].class);

            Log.d(TAG, "handleActionUnzip: cityDatas length: " + cityData.length);
            mRepository.addCitiesData(cityData);

            mCompleteListener.onParseComplete();
            mRunning = false;
        } catch (IOException | SQLiteException e) {
            e.printStackTrace();
            mCompleteListener.onParseError();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(gis != null) {
                try {
                    gis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public synchronized boolean isRunning() {
        return mRunning;
    }

    public class ParseBinder extends Binder {
        public ParseCitiesService getService() {
            return ParseCitiesService.this;
        }

        public void registerParseCompleteListener(CompleteListener listener) {
            mCompleteListener = listener;
        }

        public void unregisterParseCompleteListener() {
            mCompleteListener = null;
        }
    }
}