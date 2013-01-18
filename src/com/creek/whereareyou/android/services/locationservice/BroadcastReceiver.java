package com.creek.whereareyou.android.services.locationservice;

import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author Andrey Pereverzin
 *
 */
public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(LocationService.class.getName());
        context.startService(serviceIntent);
    }
}
