package com.creek.whereareyou.android.services.location;

import android.app.IntentService;
import android.content.Intent;

/**
 * 
 * @author andreypereverzin
 */
public class LocationUpdateService extends IntentService {
    private static final String TAG = "LocationUpdateService";
    
    public LocationUpdateService() {
        super(TAG);
    }
    
    protected void onHandleIntent(Intent intent) {
        
    }
}
