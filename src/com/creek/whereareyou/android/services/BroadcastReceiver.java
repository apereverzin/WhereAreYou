package com.creek.whereareyou.android.services;

import com.creek.whereareyou.android.services.inform.InformService;
import com.creek.whereareyou.android.services.location.LocationService;
import com.creek.whereareyou.android.services.trace.TraceService;

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
        startService(context, LocationService.class);
        startService(context, TraceService.class);
        startService(context, InformService.class);
    }
    
    private void startService(Context context, @SuppressWarnings("rawtypes") Class clazz) {
        Intent serviceIntent = new Intent(clazz.getName());
        context.startService(serviceIntent);
    }
}
