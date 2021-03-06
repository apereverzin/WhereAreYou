 package com.creek.whereareyou.android.services.receivers;

import com.creek.whereareyou.WhereAreYouApplication;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.services.email.EmailSendingAndReceivingService;
import com.creek.whereareyou.android.services.location.CurrentLocationService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class BootReceiver extends android.content.BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-onReceive");
        //SQLiteDatabase whereAreYouDatabase = SQLiteRepositoryManager.getInstance().createDatabaseIfDoesNotExist(ctx);
        //WhereAreYouApplication.setDatabase(whereAreYouDatabase);
        
        startService(ctx, EmailSendingAndReceivingService.class);
        startService(ctx, CurrentLocationService.class);
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
