package com.metalcyborg.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class ConnectivityReceiver extends BroadcastReceiver {

    private ConnectivityListener mListener;

    public interface ConnectivityListener {
        void onConnectionChanged(boolean connected);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if(mListener != null) {
            mListener.onConnectionChanged(isConnected);
        }
    }

    public void setConnectivityListener(ConnectivityListener listener) {
        mListener = listener;
    }
}
