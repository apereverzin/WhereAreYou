package com.creek.whereareyou.android.services.email;

import java.util.Timer;
import java.util.TimerTask;

import com.creek.whereareyou.android.accountaccess.GoogleAccountProvider;
import com.creek.whereareyou.android.infrastructure.sqlite.SQLiteRepositoryManager;
import com.creek.whereareyou.android.util.ActivityUtil;

import android.accounts.Account;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class EmailSendingAndReceivingService extends Service {
    private static final String TAG = EmailSendingAndReceivingService.class.getSimpleName();
    
    private static final String TIMER_NAME = "WhereAreYouEmailReceivingTimer";

    private Timer timer;
    protected ContentResolver contentResolver;
    
    private TimerTask emailSendingAndReceivingTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "===================EmailSendingAndReceivingService doing work");
            Account account = GoogleAccountProvider.getInstance().getEmailAccount(EmailSendingAndReceivingService.this);
            Log.d(TAG, "===================: " + account.toString());
            SQLiteRepositoryManager.getInstance().initialise(EmailSendingAndReceivingService.this);
            Log.d(TAG, "===================SQLiteRepositoryManager initialized");
            
            try {
                EmailSendingAndReceivingManager emailSendingAndReceivingManager = new EmailSendingAndReceivingManager(account);
                
                EmailSender emailSender = new EmailSender(emailSendingAndReceivingManager);
                emailSender.sendRequestsAndResponses();
                
                EmailReceiver emailReceiver = new EmailReceiver(emailSendingAndReceivingManager);
                emailReceiver.receiveRequestsAndResponses();
            } catch(Throwable ex) {
                ActivityUtil.showException(EmailSendingAndReceivingService.this, ex);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contentResolver = getContentResolver();

        timer = new Timer(TIMER_NAME);
        timer.schedule(emailSendingAndReceivingTask, 1000L, 30 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
