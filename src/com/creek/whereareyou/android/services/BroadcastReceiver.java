package com.creek.whereareyou.android.services;

import com.creek.whereareyou.android.locationprovider.LocationProvider;
import com.creek.whereareyou.android.services.locationservice.LocationService;

import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author Andrey Pereverzin
 */
public class BroadcastReceiver extends android.content.BroadcastReceiver {
    private static final String TAG = BroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(LocationService.class.getName());
        context.startService(serviceIntent);
    }
}
