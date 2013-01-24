package com.creek.whereareyou.android;

import android.util.Log;

import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;
import com.creek.whereareyou.android.accountaccess.MailAccountPropertiesProvider;
import com.creek.whereareyou.android.locationprovider.LocationProvider;


/**
 * 
 * @author Andrey Pereverzin
 */
public class ApplManager {
    private static final String TAG = ApplManager.class.getSimpleName();
    
    private static ApplManager instance = new ApplManager();
    private LocationProvider locationProvider = new LocationProvider();
    private GoogleAccountProvider accountProvider = new GoogleAccountProvider();
    private MailAccountPropertiesProvider mailAccountPropertiesProvider = new MailAccountPropertiesProvider();

    private ApplManager() {
        //
    }

    public static ApplManager getInstance() {
        return instance;
    }

    public LocationProvider getLocationProvider() {
        return locationProvider;
    }

    public GoogleAccountProvider getAccountProvider() {
        return accountProvider;
    }

    public MailAccountPropertiesProvider getMailAccountPropertiesProvider() {
        return mailAccountPropertiesProvider;
    }
}
