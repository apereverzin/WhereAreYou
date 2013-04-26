package com.creek.whereareyou.android.services.email;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.manager.ApplManager;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

/**
 * 
 * @author andreypereverzin
 */
public class EmailReceivingService extends Service {
    private static final String TAG = EmailReceivingService.class.getSimpleName();

    private Timer timer;
    protected ContentResolver contentResolver;
    protected ConnectivityManager cm;
    
    private TimerTask emailReceivingTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================EmailReceivingService doing work");
            ApplManager.getInstance().
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        contentResolver = getContentResolver();

        timer = new Timer("WhereAreYouInformTimer");
        timer.schedule(emailReceivingTask, 1000L, 30 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
