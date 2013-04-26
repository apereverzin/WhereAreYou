 package com.creek.whereareyou.android.services.receivers;

import com.creek.whereareyou.android.services.inform.InformService;
import com.creek.whereareyou.android.services.trace.TraceService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * 
 * @author Andrey Pereverzin
 */
public class BootReceiver extends android.content.BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        startService(context, TraceService.class);
        startService(context, InformService.class);
//        Intent activeIntent = new Intent(context, LocationChangedReceiver.class);
//        PendingIntent locationListenerPendingIntent = PendingIntent.getBroadcast(context, 0, activeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListenerPendingIntent);
    }
    
    private void startService(Context context, @SuppressWarnings("rawtypes") Class clazz) {
        Intent serviceIntent = new Intent(clazz.getName());
        context.startService(serviceIntent);
    }
}
